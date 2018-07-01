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

package com.bjzhianjia.scp.security.common.entity;

import lombok.Data;

import java.util.Date;

/**
 *
 * @author scp
 * @version 1.0
 */
@Data
public class BusinessEntity extends BaseEntity {
    private String crtUserId;
    private String crtUserName;
    private Date crtTime;

    private String updUserId;
    private String updUserName;
    private Date updTime;
}
