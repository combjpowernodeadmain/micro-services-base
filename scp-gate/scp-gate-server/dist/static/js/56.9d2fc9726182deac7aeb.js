webpackJsonp([56],{firN:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var i=n("Dd8w"),r=n.n(i),l=n("5uXp"),s=n("NYxO"),a=n("RbaZ"),o={props:{groupId:{default:"1"}},data:function(){return{filterText:"",list:null,total:null,listQuery:{name:void 0},treeData:[],defaultProps:{children:"children",label:"title"},groupManager_menu:!1,groupManager_element:!1}},watch:{filterText:function(e){this.$refs.menuTree.filter(e)}},created:function(){this.getList(),this.groupManager_menu=this.elements["groupManager:menu"],this.groupManager_element=this.elements["groupManager:element"]},computed:r()({},Object(s.b)(["elements"])),methods:{getList:function(){var e=this;Object(l.e)(this.listQuery).then(function(t){e.treeData=t,e.initAuthoritys()})},renderContent:function(e,t){var n=this,i=t.node,r=t.data;return e("span",{style:"flex: 1; display: flex; align-items: center; justify-content: space-between; font-size: 14px; padding-right: 8px;"},[e("span",[i.label]),e("span",[e("el-tooltip",{class:"item",attrs:{effect:"dark",content:"选中当前节点和所有下级节点",placement:"top-start"}},[e("el-button",{attrs:{size:"mini",type:"text"},on:{click:function(){return n.selectChildren(r)}}},[e("i",{class:"el-icon-circle-check"})])]),e("el-tooltip",{class:"item",attrs:{effect:"dark",content:"取消选中当前节点和所有下级节点",placement:"top-start"}},[e("el-button",{attrs:{size:"mini",type:"text"},on:{click:function(){return n.unSelectChildren(r)}}},[e("i",{class:"el-icon-circle-close"})])])])])},selectChildren:function(e){var t=this.$refs.menuTree.getCheckedNodes(),n=[];n.push(e.id);for(var i=0;i<t.length;i++)n.push(t[i].id);Object(a.a)(n,e),this.$refs.menuTree.setCheckedKeys(n)},unSelectChildren:function(e){var t=[];t.push(e.id),Object(a.a)(t,e);for(var n=0;n<t.length;n++)this.$refs.menuTree.setChecked(t[n],!1)},filterNode:function(e,t){return!e||-1!==t.label.indexOf(e)},getNodeData:function(e){var t=this;this.listQuery.menuId=e.id,Object(l.q)(this.listQuery).then(function(e){t.list=e.data.rows,Object(l.h)(t.groupId).then(function(e){for(var n={},i=0;i<t.list.length;i++)n[t.list[i].id]=t.list[i];for(var r={},l=0;l<e.data.length;l++){var s=e.data[l];void 0!==n[s]&&void 0===r[s]&&(t.$refs.elementTable.toggleRowSelection(n[e.data[l]]),r[s]=!0)}})}),this.currentId=e.id,this.showElement=!0},getTreeNodeKey:function(e){return e.id},handleSelectionChange:function(e,t){for(var n=this,i=!0,r=0;r<e.length;r++)if(e[r].id===t.id){Object(l.a)(this.groupId,{menuId:this.currentId,elementId:t.id}).then(function(){n.$notify({title:"成功",message:"资源权限添加成功",type:"success",duration:2e3})}),i=!1;break}i&&Object(l.s)(this.groupId,{menuId:this.currentId,elementId:t.id}).then(function(){n.$notify({title:"成功",message:"资源权限移除成功",type:"success",duration:2e3})})},handleSelectionAll:function(e){var t=this;if(0===e.length&&null!==this.list)for(var n=0;n<this.list.length;n++)Object(l.s)(this.groupId,{menuId:this.currentId,elementId:this.list[n].id}).then(function(){t.$notify({title:"成功",message:"资源权限移除成功",type:"success",duration:2e3})});else for(var i=0;i<e.length;i++)Object(l.a)(this.groupId,{menuId:this.currentId,elementId:e[i].id}).then(function(){t.$notify({title:"成功",message:"资源权限添加成功",type:"success",duration:2e3})})},update:function(){var e=this;this.$emit("closeAuthorityDialog");for(var t=this.$refs.menuTree.getCheckedNodes(),n=[],i=0;i<t.length;i++)n.push(t[i].id);Object(l.n)(this.groupId,{menuTrees:n.join()}).then(function(){e.$notify({title:"成功",message:"菜单权限保存成功",type:"success",duration:2e3})})},initAuthoritys:function(){var e=this;Object(l.j)(this.groupId).then(function(t){for(var n=[],i=0;i<t.data.length;i++)n.push(t.data[i].id);e.$refs.menuTree.setCheckedKeys(n)})}}},c={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("el-row",[n("el-col",{attrs:{span:24}},[e.groupManager_menu?n("el-button",{attrs:{type:"primary"},on:{click:e.update}},[e._v("保存")]):e._e()],1),e._v(" "),n("el-col",{staticStyle:{"margin-top":"15px","margin-right":"10px"},attrs:{span:9}},[n("el-input",{attrs:{placeholder:"输入关键字进行过滤"},model:{value:e.filterText,callback:function(t){e.filterText=t},expression:"filterText"}}),e._v(" "),n("el-tree",{ref:"menuTree",staticClass:"filter-tree",attrs:{"render-content":e.renderContent,"check-strictly":"",data:e.treeData,"show-checkbox":"","node-key":"id","expand-on-click-node":!1,"highlight-current":"",props:e.defaultProps,"filter-node-method":e.filterNode,"default-expand-all":""},on:{"node-click":e.getNodeData}})],1),e._v(" "),n("el-col",{staticStyle:{"margin-top":"15px"},attrs:{span:14}},[n("el-table",{ref:"elementTable",staticStyle:{width:"100%"},attrs:{data:e.list,border:"",fit:"","highlight-current-row":""},on:{select:e.handleSelectionChange,"select-all":e.handleSelectionAll}},[e.groupManager_element?n("el-table-column",{attrs:{type:"selection",width:"55"}}):e._e(),e._v(" "),n("el-table-column",{attrs:{width:"200px",align:"center",label:"资源类型"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("span",[e._v("\n            "+e._s(t.row.type))])]}}])}),e._v(" "),n("el-table-column",{attrs:{width:"200px",align:"center",label:"资源名称"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("span",[e._v("\n            "+e._s(t.row.name))])]}}])}),e._v(" "),n("el-table-column",{attrs:{width:"200px",align:"center",label:"资源地址"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("span",[e._v("\n            "+e._s(t.row.uri))])]}}])}),e._v(" "),n("el-table-column",{attrs:{width:"200px",align:"center",label:"资源请求类型"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("span",[e._v("\n            "+e._s(t.row.method))])]}}])})],1)],1)],1)},staticRenderFns:[]},u=n("VU/8")(o,c,!1,null,null,null);t.default=u.exports}});