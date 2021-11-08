/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.reservation;

import entity.RoomTypeEntity;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author zenyew
 */
public class Pair implements Serializable{
    
    private RoomTypeEntity roomType;
    private BigDecimal price;

    public Pair() {
    }

    public Pair(RoomTypeEntity roomType, BigDecimal price) {
        this.roomType = roomType;
        this.price = price;
    }

    /**
     * @return the roomType
     */
    public RoomTypeEntity getRoomType() {
        return roomType;
    }

    /**
     * @param roomType the roomType to set
     */
    public void setRoomType(RoomTypeEntity roomType) {
        this.roomType = roomType;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    
}
