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

package com.bjzhianjia.scp.security.admin.vo;

import java.util.List;

import com.bjzhianjia.scp.security.api.vo.authority.PermissionInfo;

/**
 * Created by scp on 2017/8/22.
 */
public class FrontUser {
    public String id;
    public String username;
    public String name;
    private String description;
    private String image;
    private List<PermissionInfo> menus;
    private List<PermissionInfo> elements;
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public List<PermissionInfo> getMenus() {
        return menus;
    }

    public void setMenus(List<PermissionInfo> menus) {
        this.menus = menus;
    }

    public List<PermissionInfo> getElements() {
        return elements;
    }

    public void setElements(List<PermissionInfo> elements) {
        this.elements = elements;
    }
}
