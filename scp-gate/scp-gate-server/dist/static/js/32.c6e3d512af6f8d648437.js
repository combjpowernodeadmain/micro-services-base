webpackJsonp([32],{ViOJ:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r=n("Dd8w"),i=n.n(r),a=n("yMJ4"),s=n("tajs"),l=n("RbaZ"),o=n("NYxO"),c={props:{positionId:{default:"1"}},data:function(){return{filterText:"",list:null,total:null,listQuery:{name:void 0},treeData:[],defaultProps:{children:"children",label:"label"},positionManager_btn_depart:!1}},watch:{filterText:function(e){this.$refs.departTree.filter(e)}},created:function(){this.getList(),this.positionManager_btn_depart=this.elements["positionManager:btn_depart"]},computed:i()({},Object(o.b)(["elements"])),methods:{getList:function(){var e=this;Object(a.f)(this.listQuery).then(function(t){e.treeData=t,e.initDeparts()})},filterNode:function(e,t){return!e||-1!==t.label.indexOf(e)},getNodeData:function(e){this.listQuery.menuId=e.id,this.currentId=e.id,this.showElement=!0},getTreeNodeKey:function(e){return e.id},renderContent:function(e,t){var n=this,r=t.node,i=t.data;return e("span",{style:"flex: 1; display: flex; align-items: center; justify-content: space-between; font-size: 14px; padding-right: 8px;"},[e("span",[r.label]),e("span",[e("el-tooltip",{class:"item",attrs:{effect:"dark",content:"选中当前节点和所有下级节点",placement:"top-start"}},[e("el-button",{attrs:{size:"mini",type:"text"},on:{click:function(){return n.selectChildren(i)}}},[e("i",{class:"el-icon-circle-check"})])]),e("el-tooltip",{class:"item",attrs:{effect:"dark",content:"取消选中当前节点和所有下级节点",placement:"top-start"}},[e("el-button",{attrs:{size:"mini",type:"text"},on:{click:function(){return n.unSelectChildren(i)}}},[e("i",{class:"el-icon-circle-close"})])])])])},selectChildren:function(e){var t=this.$refs.departTree.getCheckedNodes(),n=[];n.push(e.id);for(var r=0;r<t.length;r++)n.push(t[r].id);Object(l.a)(n,e),this.$refs.departTree.setCheckedKeys(n)},unSelectChildren:function(e){var t=[];t.push(e.id),Object(l.a)(t,e);for(var n=0;n<t.length;n++)this.$refs.departTree.setChecked(t[n],!1)},update:function(){for(var e=this,t=this.$refs.departTree.getCheckedNodes(),n=[],r=0;r<t.length;r++)n.push(t[r].id);Object(s.g)(this.positionId,{departs:n.join()}).then(function(){e.$refs.departTree.setCheckedKeys([]),e.$emit("closeDepartDialog"),e.$notify({title:"成功",message:"关联部门成功",type:"success",duration:2e3})})},initDeparts:function(){var e=this;Object(s.c)(this.positionId).then(function(t){for(var n=[],r=0;r<t.data.length;r++)n.push(t.data[r].id);e.$refs.departTree.setCheckedKeys(n)})}}},d={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("el-row",[n("el-col",{attrs:{span:24}},[e.positionManager_btn_depart?n("el-button",{attrs:{type:"primary"},on:{click:e.update}},[e._v("保存")]):e._e()],1),e._v(" "),n("el-col",{staticStyle:{"margin-top":"15px","margin-right":"10px"},attrs:{span:24}},[n("el-input",{attrs:{placeholder:"输入关键字进行过滤"},model:{value:e.filterText,callback:function(t){e.filterText=t},expression:"filterText"}}),e._v(" "),n("el-tree",{ref:"departTree",staticClass:"filter-tree",attrs:{"render-content":e.renderContent,"node-key":"id","check-strictly":"",data:e.treeData,"show-checkbox":"","expand-on-click-node":!1,"highlight-current":"",props:e.defaultProps,"filter-node-method":e.filterNode,"default-expand-all":""},on:{"node-click":e.getNodeData}})],1)],1)},staticRenderFns:[]},p=n("VU/8")(c,d,!1,null,null,null);t.default=p.exports}});