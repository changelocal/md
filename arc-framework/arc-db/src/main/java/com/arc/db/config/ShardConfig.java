package com.arc.db.config;

import com.alibaba.fastjson.annotation.JSONField;
import com.arc.util.config.ConfigException;
import com.arc.util.convert.DateConverter;
import com.arc.util.convert.StringConverter;
import com.arc.util.lang.FaultException;
import com.arc.util.lang.UncheckedException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public final class ShardConfig {
    private Map<String, Node> nodes = new ConcurrentHashMap<>();

    public static ShardConfig getDefault() {
        return Holder.defaultInstance;
    }

    private ShardConfig() {
    }

    public ShardConfig(String filePath) {
        //<nodes>
        //  <node name="Order" shard="range">
        //      <node read="Order1_R" write="Order1_W" shard="none" start="1" end="1000000"/>
        //      <node read="Order2_R" write="Order2_W" shard="none" start="1000000" end="2000000"/>
        //  </node>
        //  <node name="UserOrder" shard="time">
        //      <node shard="hash" count="2" start="2015-10-01" end="2015-11-01">
        //          <node read="Order1_1_R" write="Order1_1_W" shard="none" mod="0"/>
        //          <node read="Order1_2_R" write="Order1_2_W" shard="none" mod="1"/>
        //      </node>
        //      <node shard="hash" count="2" start="2015-11-01" end="2015-12-01">
        //          <node read="Order2_1_R" write="Order2_1_W" shard="none" mod="0"/>
        //          <node read="Order2_2_R" write="Order2_2_W" shard="none" mod="1"/>
        //      </node>
        //  </node>
        //</nodes>
        try {
            /*Document doc = ConfigParser.resolveXml(filePath);

            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                if (!(nodeList.item(i) instanceof Element)) continue;

                Element elem = (Element) nodeList.item(i);
                ShardInfo shard = new ShardInfo(elem);
                this.nodes.put(shard.name, new Node(null, shard));
            }*/
        } catch (Exception e) {
            throw new ConfigException(e);
        }
    }

    /**
     * 获取分片节点
     *
     * @param name 名称
     * @param keys 分片参数
     * @return
     */
    public Node getNode(String name, Object... keys) {
        Node node = findNode(name);
        if (node == null) {
            throw error(name, keys);
        }

        int level = 0;
        while (node.nodes != null && !node.nodes.isEmpty()) {
            node = node.getNode(keys[level++]);
            if (node == null) throw error(name, keys);
        }
        return node;
    }

    /**
     * 获取所有分片节点列表
     *
     * @param name 名称
     * @return
     */
    public List<Node> getNodes(String name) {
        Node node = findNode(name);
        if (node == null) {
            throw error(name);
        }

        List<Node> list = new ArrayList<>();
        fillLeafNodes(list, node);
        return list;
    }

    /**
     * 获取指定参数下的分片节点列表
     *
     * @param name 名称
     * @param keys 分片参数
     * @return
     */
    public List<Node> getNodes(String name, Object... keys) {
        Node node = findNode(name);
        if (node == null) {
            throw error(name);
        }

        List<Node> list = new ArrayList<>();
        if (keys != null) {
            int level = 0;
            while (node.nodes != null && !node.nodes.isEmpty() && level < keys.length) {
                node = node.getNode(keys[level++]);
                if (node == null) throw error(name, keys);
            }
        }
        fillLeafNodes(list, node);
        return list;
    }

    private void fillLeafNodes(List<Node> nodes, Node node) {
        if (node.nodes == null) {
            nodes.add(node);
        } else {
            for (Node n : node.nodes) {
                fillLeafNodes(nodes, n);
            }
        }
    }

    private Node findNode(String name) {
        Node node = this.nodes.get(name);
        if (node == null) {
            //node = getNodeFromRemote(name);
            this.nodes.put(name, node);
        }

        if (node == Node.EMPTY) {
            return null;
        }

        return node;
    }

    /*private synchronized Node getNodeFromRemote(String name) {
        Node node = this.nodes.get(name);
        if (node != null) return node;

        Map<String, String> m = new HashMap<>();
        m.put("name", name);
        RemoteLoader.Result result = RemoteLoader.load("/db/shard", m);
        if (!result.isSuccess()) {
            logger.error("config > db.shard: load remote config of {} failed: {}", name, result.getError());
            return Node.EMPTY;
        }

        ShardInfo shardInfo = JsonEncoder.DEFAULT.decode(result.getValue(), ShardInfo.class);
        return new Node(null, shardInfo);
    }*/

    private static UncheckedException error(String name, Object... keys) {
        String msg;
        if (keys == null) {
            msg = String.format("找不到匹配的节点, name: %s", name);
        } else {
            msg = String.format("找不到匹配的节点, name: %s, keys: %s", name, StringConverter.toString("-", keys));
        }
        return new UncheckedException(msg);
    }

    /**
     * 规则接口
     */
    public interface Rule {
        Object getMatchKey(Object key);
    }

    /**
     * 匹配基类
     */
    public interface Match {
        boolean isMatch(Object key);
    }

    /**
     * Hash 规则
     */
    public static class HashRule implements Rule {
        private int count;

        private HashRule(int count) {
            this.count = count;
        }

        @Override
        public Object getMatchKey(Object key) {
            int hash = key.hashCode();
            return Math.abs(hash % count);
        }
    }

    /**
     * Hash 匹配
     */
    public static class HashMatch implements Match {
        private int[] remainders;

        private HashMatch(int[] remainders) {
            this.remainders = remainders;
        }

        @Override
        public boolean isMatch(Object key) {
            int remainder = (int) key;
            for (int r : remainders) {
                if (r == remainder) return true;
            }
            return false;
        }
    }

    public static class ConsistentHashRule implements Rule {
        //hash环默认虚拟节点总数
        private static final int NODE_NUM = 65536;
        private TreeMap<Long, Integer> nodes = new TreeMap<Long, Integer>();
        private int index = 0;

        public ConsistentHashRule(int size) {
            if (!isVaild(size)) {
                throw new FaultException("consistent hash node count is error");
            }

            ArrayList<Integer> idLinked = getIdLinked();
            init(idLinked);
        }

        private boolean isVaild(int num) {
            if (num < 2) {
                return false;
            }

            while (true) {
                if (num == 1) {
                    return true;
                }

                if (num % 2 != 0) {
                    return false;
                } else {
                    num /= 2;
                    index++;
                }
            }
        }

        private ArrayList<Integer> getIdLinked() {
            ArrayList<Integer> leftNodes = new ArrayList<>();
            ArrayList<Integer> rightNodes = new ArrayList<>();

            int level = 0;
            int maxValue = 0;
            while (true) {
                if (level >= index) {
                    break;
                }

                int leftCount = 0;
                int rightCount = 0;
                int loopCount = 0;
                for (int i = maxValue; i < Math.pow(2, (level + 1)); i++) {
                    if (loopCount == 0 || loopCount < maxValue / 2) {
                        if (leftNodes.size() == 0) {
                            leftNodes.add(i + 1);
                        } else {
                            leftNodes.add(leftCount * 2 + 1, i + 1);
                        }
                        leftCount++;
                    } else {
                        if (rightNodes.size() == 0) {
                            rightNodes.add(i + 1);
                        } else {
                            rightNodes.add(rightCount * 2 + 1, i + 1);
                        }
                        rightCount++;
                    }
                    loopCount++;
                }
                maxValue = new Double(Math.pow(2, (level + 1))).intValue();
                level++;
            }
            leftNodes.addAll(rightNodes);
            return leftNodes;
        }

        private void init(ArrayList<Integer> idLinked) {
            int avgValue = NODE_NUM / idLinked.size();
            //固定总hash环大小为65536
            for (int i = 0; i < NODE_NUM; i++) {
                int n = i / avgValue;
                //nodes.put(Hashing.MURMUR_HASH.hash(("SHARD-NODE-" + (i + 1))), idLinked.get(n));
            }
        }

        @Override
        public Object getMatchKey(Object key) {
            Long hash = 0L; //Hashing.MURMUR_HASH.hash(String.valueOf(key));
            SortedMap<Long, Integer> sortedMap = nodes.tailMap(hash);
            if (sortedMap.size() == 0) {
                return nodes.get(nodes.firstKey());
            }
            return nodes.get(sortedMap.firstKey());
        }
    }

    public static class ConsistentHashMatch implements Match {
        private int num;

        public ConsistentHashMatch(int num) {
            this.num = num;
        }

        @Override
        public boolean isMatch(Object key) {
            int ikey = (int) key;
            return ikey == num;
        }
    }

    /**
     * 整数范围规则
     */
    public static class IntRangeRule implements Rule {
        @Override
        public Object getMatchKey(Object key) {
            return ((Number) key).longValue();
        }
    }

    /**
     * 整数范围匹配
     */
    public static class IntRangeMatch implements Match {
        private long start;
        private long end;

        private IntRangeMatch(long start, long end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public boolean isMatch(Object key) {
            long value = (long) key;
            return value >= start && value < end;
        }
    }

    /**
     * 时间规则
     */
    public static class DateRangeRule implements Rule {
        @Override
        public Object getMatchKey(Object key) {
            Class<?> clazz = key.getClass();
            if (clazz == Date.class) {
                return key;
            } else if (clazz == LocalDateTime.class) {
                return DateConverter.toDate((LocalDateTime) key);
            }
            throw new IllegalArgumentException("key type must be Date or LocalDateTime");
        }
    }

    /**
     * 时间匹配
     */
    public static class DateRangeMatch implements Match {
        private Date start;
        private Date end;

        public DateRangeMatch(Date start, Date end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public boolean isMatch(Object key) {
            Date value = (Date) key;
            return value.compareTo(start) >= 0 && value.before(end);
        }
    }

    /**
     * 分片节点
     */
    public static class Node {
        private static final Node EMPTY = new Node();

        @Getter
        private String read;
        @Getter
        private String write;
        @Getter
        private String suffix;
        private String type;
        private List<Node> nodes;
        private Rule rule;
        private Match match;

        private Node getNode(Object key) {
            Object matchKey = rule.getMatchKey(key);
            for (Node n : nodes) {
                if (n.match.isMatch(matchKey)) return n;
            }
            return null;
        }

        private Node() {
        }

        private Node(Node parent, NodeInfo ni) {
            this.type = ni.type;
            if (ni.args != null) {
                this.read = ni.args.get("read");
                this.write = ni.args.get("write");
                this.suffix = ni.args.get("suffix");
            }
            this.rule = createRule(ni.type, ni.args);
            if (parent != null) {
                this.match = createMatch(parent.type, ni.args);
            }

            if (ni.nodes != null && !ni.nodes.isEmpty()) {
                this.nodes = new ArrayList<>(ni.nodes.size());
                ni.nodes.forEach(p -> this.nodes.add(new Node(this, p)));
            }
        }

        private static Rule createRule(String type, Map<String, String> args) {
            if (type == null) {
                return null;
            }

            switch (type) {
                case "hash":
                    int count = StringConverter.toInt32(args.get("mod"));
                    return new HashRule(count);
                case "date-range":
                    return new DateRangeRule();
                case "int-range":
                    return new IntRangeRule();
                case "consistent-hash":
                    return new ConsistentHashRule(StringConverter.toInt32(args.get("size")));
                case "":
                case "none":
                    return null;
                default:
                    throw new ConfigException("unsupported rule type: " + type);
            }
        }

        private static Match createMatch(String type, Map<String, String> args) {
            if (type == null) {
                return null;
            }

            switch (type) {
                case "hash":
                    return new HashMatch(StringConverter.toIntArray(args.get("remainders"), ","));
                case "date-range":
                    return new DateRangeMatch(parseDate(args.get("start")), parseDate(args.get("end")));
                case "int-range":
                    return new IntRangeMatch(StringConverter.toInt64(args.get("start")), StringConverter.toInt64(args.get("end")));
                case "consistent-hash":
                    return new ConsistentHashMatch(StringConverter.toInt32(args.get("num")));
                case "":
                case "none":
                    return null;
                default:
                    throw new ConfigException("unsupported match type: " + type);
            }
        }

        private static Date parseDate(String text) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                return format.parse(text);
            } catch (Exception e) {
                throw new ConfigException("invalid date value: " + text, e);
            }
        }
    }

    @Getter
    @Setter
    public static class ShardInfo extends NodeInfo {
        private String name;
        @JSONField(name = "class")
        private String clazz;
        private String version;

        public ShardInfo() {
            // for decode
        }

        public ShardInfo(Element elem) {
            super(elem);
            this.name = elem.getAttribute("name");
            this.clazz = elem.getAttribute("class");
            this.version = elem.getAttribute("version");
        }
    }

    @Getter
    @Setter
    public static class NodeInfo {
        private String type;
        private String note;
        private Map<String, String> args;
        private List<NodeInfo> nodes;

        public NodeInfo() {
            // for decode
        }

        public NodeInfo(Element elem) {
            NamedNodeMap attributes = elem.getAttributes();
            for (int i = 0; i < attributes.getLength(); i++) {
                org.w3c.dom.Node attr = attributes.item(i);
                String name = attr.getNodeName();
                String value = attr.getNodeValue();
                switch (name) {
                    case "name":
                        break;
                    case "type":
                        this.type = value;
                        break;
                    case "note":
                        this.note = value;
                        break;
                    default:
                        if (args == null) {
                            args = new HashMap<>();
                        }
                        args.put(name, value);
                        break;
                }
            }

            if (elem.hasChildNodes()) {
                NodeList childNodes = elem.getChildNodes();
                List<NodeInfo> list = new ArrayList<>(childNodes.getLength());
                for (int i = 0; i < childNodes.getLength(); i++) {
                    if (!(childNodes.item(i) instanceof Element)) continue;

                    NodeInfo n = new NodeInfo((Element) childNodes.item(i));
                    list.add(n);
                }
                if (!list.isEmpty()) this.nodes = list;
            }
        }
    }

    private static class Holder {
        private static ShardConfig defaultInstance;

        static {
            String filePath = "";//ConfigManager.findConfigPath("shard", ".conf", ".dubbo");
            if (filePath == null) {
                defaultInstance = new ShardConfig();
            } else {
                try {
                    defaultInstance = new ShardConfig(filePath);
                } catch (ConfigException e) {
                    log.error("load shard config failed", e);
                }
            }
        }
    }
}
