webpackJsonp([29],{"7vPw":function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var n=a("Dd8w"),r=a.n(n),i=a("yMJ4"),l=a("tajs"),s=a("NYxO"),o=a("yBzI"),d={watch:{filterText:function(e){this.$refs.mainTree.filter(e)}},methods:{filterNode:function(e,t){return!e||-1!==t.label.indexOf(e)},handleTableRowCreate:function(){},handleTableRowDelete:function(){},handlerTableRowFilter:function(){},handlerMainTreeForm:function(e){var t=this;this.$refs[e].validate(function(e){if(!e)return!1;Object(i.h)(t.mainTreeForm.id,t.mainTreeForm).then(function(){t.editTreeFormFlag=!1,t.initmainTreeData(),t.$notify({title:"成功",message:"修改成功",type:"success",duration:2e3})})})},append:function(e,t){var a=this,n={};Object(i.a)({name:"子节点",code:t.code+"_tmp",parentId:t.id}).then(function(e){var r={id:(n=e.data).id,label:n.name,children:[]};t.children||a.$set(t,"children",[]),t.children.push(r)}),this.$refs.mainTree.setCurrentKey(t.id)},remove:function(e,t){var a=this;if(t.children.length>0)this.$notify({title:"失败",message:"删除失败，该项有子孙节点！",type:"warning",duration:2e3});else{var n={departId:this.currentTreeNodeId};Object(l.j)(n).then(function(n){a.childTableData=n.data.rows,a.childTableData.length>0?a.$notify({title:"失败",message:"删除失败，该项有字典值！",type:"warning",duration:2e3}):(Object(i.c)(t.id).then(function(){a.$notify({title:"成功",message:"删除成功",type:"success",duration:2e3});var n=e.parent,r=n.data.children||n.data,i=r.findIndex(function(e){return e.id===t.id});r.splice(i,1)}),a.$refs.mainTree.setCurrentKey(t.id))})}},edit:function(e,t){var a=this;this.editTreeFormFlag=!0,Object(i.e)(t.id).then(function(e){a.mainTreeForm=e.data}),this.$refs.mainTree.setCurrentKey(t.id)},resetMainTreeForm:function(){this.mainTreeForm={code:"",name:""}},renderContent:function(e,t){var a=this,n=t.node,r=t.data;return e("span",{style:"flex: 1; display: flex; align-items: center; justify-content: space-between; font-size: 14px; padding-right: 8px;"},[e("span",[e("span",[n.label])]),e("span",[this.departManager_btn_edit?e("el-button",{style:"font-size: 12px;",attrs:{type:"text"},on:{click:function(){return a.edit(n,r)}}},[e("i",{class:"el-icon-edit"})]):e("span"),this.departManager_btn_add?e("el-button",{style:"font-size: 12px;",attrs:{type:"text"},on:{click:function(){return a.append(n,r)}}},[e("i",{class:"el-icon-circle-plus-outline"})]):e("span"),this.departManager_btn_del?e("el-button",{style:"font-size: 12px;",attrs:{type:"text"},on:{click:function(){return a.remove(n,r)}}},[e("i",{class:"el-icon-delete"})]):e("span")])])},initmainTreeData:function(){var e=this;Object(i.f)().then(function(t){e.mainTreeData=t})},getTreeNodeData:function(e){this.currentTreeNodeId=e.id,this.currentTreeNode=e}},created:function(){var e=this;this.initmainTreeData(),this.departManager_btn_edit=this.elements["departManager:btn_edit"],this.departManager_btn_del=this.elements["departManager:btn_del"],this.departManager_btn_add=this.elements["departManager:btn_add"],Object(o.c)("org_depart").then(function(t){e.departTypeOptions=t.data.rows})},computed:r()({},Object(s.b)(["elements"])),data:function(){return{departTypeOptions:[],departManager_btn_edit:!0,departManager_btn_del:!0,departManager_btn_add:!0,filterText:"",editTreeFormFlag:!1,currentTreeNodeId:void 0,currentTreeNode:{},mainTreeForm:{code:"",name:""},listTableQuery:{name:void 0,departId:void 0},mainTreeFormRules:{code:[{required:!0,message:"请输入编码",trigger:"blur"},{min:1,max:20,message:"长度在 1 到 20 个字符",trigger:"blur"}],name:[{required:!0,message:"请输入目录名",trigger:"blur"},{min:1,max:20,message:"长度在 1 到 20 个字符",trigger:"blur"}]},mainTreeData:[],defaultProps:{children:"children",label:"label"}}}},c={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"app-container calendar-list-container"},[a("el-row",[a("el-col",{attrs:{span:8}},[a("div",{staticClass:"grid-content bg-purple",staticStyle:{"margin-right":"10px"}},[a("el-card",{staticClass:"box-card"},[a("div",{staticClass:"clearfix",attrs:{slot:"header"},slot:"header"},[a("span",[e._v("组织架构")])]),e._v(" "),a("div",[a("el-input",{attrs:{placeholder:"输入关键字进行过滤"},model:{value:e.filterText,callback:function(t){e.filterText=t},expression:"filterText"}}),e._v(" "),a("el-tree",{ref:"mainTree",staticClass:"filter-tree",attrs:{"node-key":"id",data:e.mainTreeData,props:e.defaultProps,"default-expand-all":"","expand-on-click-node":!1,"filter-node-method":e.filterNode,"highlight-current":"","render-content":e.renderContent},on:{"node-click":e.getTreeNodeData}})],1)])],1)]),e._v(" "),a("el-col",{staticStyle:{height:"100%"},attrs:{span:16}},[a("div",{staticClass:"grid-content bg-purple-light"},[a("el-tabs",{on:{"tab-click":e.handleClick},model:{value:e.activeName,callback:function(t){e.activeName=t},expression:"activeName"}},[a("el-tab-pane",{attrs:{label:"用户列表",name:"first"}},[e._v("用户列表")]),e._v(" "),a("el-tab-pane",{attrs:{label:"职位列表",name:"second"}},[e._v("职位列表")])],1),e._v(" "),e.editTreeFormFlag?a("el-card",{staticClass:"box-card"},[a("div",{staticClass:"clearfix",attrs:{slot:"header"},slot:"header"},[a("span",[e._v("部门编辑")])]),e._v(" "),a("div",[a("el-form",{ref:"mainTreeForm",staticClass:"demo-ruleForm",attrs:{model:e.mainTreeForm,"status-icon":"",rules:e.mainTreeFormRules,"label-width":"100px"}},[a("el-form-item",{attrs:{label:"编码",prop:"code"}},[a("el-input",{attrs:{placeholder:"请输入编码"},model:{value:e.mainTreeForm.code,callback:function(t){e.$set(e.mainTreeForm,"code",t)},expression:"mainTreeForm.code"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"部门名",prop:"name"}},[a("el-input",{attrs:{placeholder:"请输入部门名"},model:{value:e.mainTreeForm.name,callback:function(t){e.$set(e.mainTreeForm,"name",t)},expression:"mainTreeForm.name"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"部门类型"}},[a("el-select",{staticClass:"filter-item",attrs:{placeholder:"请选择部门类型"},model:{value:e.mainTreeForm.type,callback:function(t){e.$set(e.mainTreeForm,"type",t)},expression:"mainTreeForm.type"}},e._l(e.departTypeOptions,function(e){return a("el-option",{key:e.id,attrs:{label:e.labelDefault,value:e.value}})}))],1),e._v(" "),a("el-form-item",[this.departManager_btn_edit?a("el-button",{attrs:{type:"primary"},on:{click:function(t){e.handlerMainTreeForm("mainTreeForm")}}},[e._v("提交")]):e._e(),e._v(" "),a("el-button",{on:{click:function(t){e.editTreeFormFlag=!1}}},[e._v("取消")])],1)],1)],1)]):e._e()],1)])],1)],1)},staticRenderFns:[]},m=a("VU/8")(d,c,!1,null,null,null);t.default=m.exports}});