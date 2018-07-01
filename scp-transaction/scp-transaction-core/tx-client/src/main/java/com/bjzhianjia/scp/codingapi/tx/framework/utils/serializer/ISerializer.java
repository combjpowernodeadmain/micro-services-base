/*
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.

 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */


package com.bjzhianjia.scp.codingapi.tx.framework.utils.serializer;


import com.lorne.core.framework.exception.SerializerException;

/**
 * @author lorne 2017/11/11
 */
public interface ISerializer {
    /**
     * 序列化对象
     *
     * @param obj 需要序更列化的对象
     * @return byte []  序列号结果
     * @throws SerializerException  序列化异常
     */
    byte[] serialize(Object obj) throws SerializerException;


    /**
     * 反序列化对象
     *
     * @param param 需要反序列化的byte []
     * @param clazz 反序列化成为的bean对象Class
     * @param <T>   反序列化成为的bean对象
     * @return  对象
     * @throws SerializerException  序列化异常
     */

    <T> T deSerialize(byte[] param, Class<T> clazz) throws SerializerException;
}
