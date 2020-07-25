package com.arc.util.data;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;
import java.util.*;

/**
 * 分页结果
 *
 * @author xfwang
 */
@JsonSerialize(using = PageResult.PageResultSerializer.class)
public class PageResult<T> implements PageResultSet<T>, Collection<T> {

    private static final PageResult EMPTY_PAGE = new EmptyPageResult<>();

    /**
     * 当前页数据列表
     */
    private List<T> items;

    /**
     * 总记录数
     */
    private int totalCount;

    public PageResult() {
        // default ctor
    }

    public PageResult(List<T> items, int totalCount) {
        this.items = items;
        this.totalCount = totalCount;
    }

    @Override
    public List<T> getItems() {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        return this.items;
    }

    @Override
    public void setItems(List<T> items) {
        this.items = items;
    }

    @Override
    public int getTotalCount() {
        return totalCount;
    }

    @Override
    public void setTotalCount(int totalCount) {
        if (totalCount < 0) {
            throw new IllegalArgumentException("totalCount can't be negative.");
        }
        this.totalCount = totalCount;
    }

    @Override
    public int size() {
        return this.items == null ? 0 : this.items.size();
    }

    @Override
    public boolean isEmpty() {
        return this.items == null || this.items.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.items != null && this.items.contains(o);
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<T> iterator() {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        return this.items.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.items == null ? new Object[0] : this.items.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return this.items == null ? new ArrayList<T>().toArray(a) : this.items.toArray(a);
    }

    @Override
    public boolean add(T e) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        return this.items.add(e);
    }

    @Override
    public boolean remove(Object o) {
        if (this.items == null) {
            return false;
        }
        return this.items.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.items != null && this.items.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        return this.items.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return this.items != null && this.items.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.items != null && this.items.retainAll(c);
    }

    @Override
    public void clear() {
        if (items != null) {
            this.items.clear();
        }
    }

    public static <T> PageResult<T> emptyPage() {
        return EMPTY_PAGE;
    }

    private static class EmptyPageResult<T> extends PageResult<T> {

        EmptyPageResult() {
            super(Collections.emptyList(), 0);
        }

        @Override
        public void setItems(List<T> items) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setTotalCount(int totalCount) {
            throw new UnsupportedOperationException();
        }

    }

    public static class PageResultSerializer extends JsonSerializer<PageResult<?>> {
        @Override
        public void serialize(PageResult<?> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStartObject();
            gen.writeNumberField("totalCount", value.totalCount);
            if (value.items == null) {
                gen.writeNullField("items");
            } else {
                gen.writeArrayFieldStart("items");
                for (int i = 0; i < value.items.size(); i++) {
                    Object item = value.items.get(i);
                    gen.writeObject(item);
                }
                gen.writeEndArray();
            }
            gen.writeEndObject();
        }
    }
}
