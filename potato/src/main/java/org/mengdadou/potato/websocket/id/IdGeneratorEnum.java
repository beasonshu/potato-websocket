package org.mengdadou.potato.websocket.id;

import org.mengdadou.potato.websocket.util.SpiUtil;

/**
 * Created by mengdadou on 17-10-16.
 */
public enum  IdGeneratorEnum {
    INST;
    private IdGenerator idGenerator;
    
    IdGeneratorEnum() {
        this.idGenerator = SpiUtil.getServiceImpl(IdGenerator.class);
    }
    
    public IdGenerator getIdGenerator() {
        return idGenerator;
    }
}
