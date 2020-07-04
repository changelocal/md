package com.arc.db.jsd.clause;


import com.arc.db.jsd.UpdateValues;

public interface UpdateClause {
    SetClause set(UpdateValues values);
}
