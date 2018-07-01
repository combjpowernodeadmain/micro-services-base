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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.bjzhianjia.scp.security.common.vo.TreeNodeVO;

/**
 * ${DESCRIPTION}
 *
 * @author scp
 * @version 1.0 
 */
public class AuthorityMenuTree extends TreeNodeVO<AuthorityMenuTree> implements Serializable{
    String text;
    List<AuthorityMenuTree> nodes = new ArrayList<AuthorityMenuTree>();
    String icon;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public AuthorityMenuTree(String text, List<AuthorityMenuTree> nodes) {
        this.text = text;
        this.nodes = nodes;
    }

    public AuthorityMenuTree() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<AuthorityMenuTree> getNodes() {
        return nodes;
    }

    public void setNodes(List<AuthorityMenuTree> nodes) {
        this.nodes = nodes;
    }

//    @Override
//    public void setChildren(List<TreeNodeVO> children) {
//        super.setChildren(children);
//        nodes = new ArrayList<AuthorityMenuTree>();
//    }
//
//    @Override
//    public void add(TreeNodeVO node) {
//        super.add(node);
//        AuthorityMenuTree n = new AuthorityMenuTree();
//        BeanUtils.copyProperties(node,n);
//        nodes.add(n);
//    }
}
