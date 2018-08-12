webpackJsonp([51],{"6Vqe":function(e,t,r){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var n=r("Dd8w"),i=r.n(n),a=r("vLgD");var o=r("NYxO"),l=r("yBzI"),d={components:{"menu-element":function(){return r.e(5).then(r.bind(null,"dJ1a"))}},data:function(){return{filterText:"",list:null,total:null,formEdit:!0,formAdd:!0,formStatus:"",showElement:!1,typeOptions:[],listQuery:{name:void 0},treeData:[],defaultProps:{children:"children",label:"title"},labelPosition:"right",form:{code:void 0,title:void 0,parentId:void 0,href:void 0,icon:void 0,orderNum:void 0,description:void 0,path:void 0,enabled:void 0,type:"authority_menu_menu",attr1:void 0},currentId:-1,menuManager_btn_add:!1,menuManager_btn_edit:!1,menuManager_btn_del:!1}},watch:{filterText:function(e){this.$refs.menuTree.filter(e)}},created:function(){var e=this;this.getList(),this.menuManager_btn_add=this.elements["menuManager:btn_add"],this.menuManager_btn_del=this.elements["menuManager:btn_del"],this.menuManager_btn_edit=this.elements["menuManager:btn_edit"],Object(l.c)("authority_menu").then(function(t){e.typeOptions=t.data.rows})},computed:i()({},Object(o.b)(["elements"])),methods:{getList:function(){var e,t=this;(e=this.listQuery,Object(a.a)({url:"/api/admin/menu/tree",method:"get",params:e})).then(function(e){t.treeData=e})},filterNode:function(e,t){return!e||-1!==t.label.indexOf(e)},getNodeData:function(e){var t,r=this;this.formEdit||(this.formStatus="update"),(t=e.id,Object(a.a)({url:"/api/admin/menu/"+t,method:"get"})).then(function(e){r.form=e.data}),this.currentId=e.id,this.showElement=!0,this.$refs.menuElement.menuId=e.id,this.$refs.menuElement.getList()},handlerEdit:function(){this.form.id&&(this.formEdit=!1,this.formStatus="update")},handlerAdd:function(){this.resetForm(),this.formEdit=!1,this.formStatus="create"},handleDelete:function(){var e=this;this.$confirm("此操作将永久删除, 是否继续?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){var t;(t=e.currentId,Object(a.a)({url:"/api/admin/menu/"+t,method:"delete"})).then(function(){e.getList(),e.resetForm(),e.onCancel(),e.$notify({title:"成功",message:"删除成功",type:"success",duration:2e3})})})},update:function(){var e,t,r=this;(e=this.form.id,t=this.form,Object(a.a)({url:"/api/admin/menu/"+e,method:"put",data:t})).then(function(){r.getList(),r.$notify({title:"成功",message:"更新成功",type:"success",duration:2e3})})},create:function(){var e,t=this;(e=this.form,Object(a.a)({url:"/api/admin/menu",method:"post",data:e})).then(function(){t.getList(),t.$notify({title:"成功",message:"创建成功",type:"success",duration:2e3})})},onCancel:function(){this.formEdit=!0,this.formStatus=""},resetForm:function(){this.form={code:void 0,title:void 0,parentId:this.currentId,href:void 0,icon:void 0,orderNum:void 0,description:void 0,path:void 0,enabled:void 0}}}},s={render:function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{staticClass:"app-container calendar-list-container"},[r("div",{staticClass:"filter-container"},[r("el-button-group",[e.menuManager_btn_add?r("el-button",{attrs:{type:"primary",icon:"plus"},on:{click:e.handlerAdd}},[e._v("添加")]):e._e(),e._v(" "),e.menuManager_btn_edit?r("el-button",{attrs:{type:"primary",icon:"edit"},on:{click:e.handlerEdit}},[e._v("编辑")]):e._e(),e._v(" "),e.menuManager_btn_del?r("el-button",{attrs:{type:"primary",icon:"delete"},on:{click:e.handleDelete}},[e._v("删除")]):e._e()],1)],1),e._v(" "),r("el-row",[r("el-col",{staticStyle:{"margin-top":"15px","margin-right":"10px"},attrs:{span:7}},[r("el-card",{staticClass:"box-card"},[r("el-input",{attrs:{placeholder:"输入关键字进行过滤"},model:{value:e.filterText,callback:function(t){e.filterText=t},expression:"filterText"}}),e._v(" "),r("el-tree",{ref:"menuTree",staticClass:"filter-tree",attrs:{data:e.treeData,"node-key":"id","highlight-current":"",props:e.defaultProps,"expand-on-click-node":!1,"filter-node-method":e.filterNode,"default-expand-all":""},on:{"node-click":e.getNodeData}})],1)],1),e._v(" "),r("el-col",{staticStyle:{"margin-top":"15px"},attrs:{span:16}},[r("el-card",{staticClass:"box-card"},[r("el-form",{ref:"form",attrs:{"label-position":e.labelPosition,"label-width":"80px",model:e.form}},[r("el-form-item",{attrs:{label:"路径编码",prop:"code"}},[r("el-input",{attrs:{disabled:e.formEdit,placeholder:"请输入路径编码"},model:{value:e.form.code,callback:function(t){e.$set(e.form,"code",t)},expression:"form.code"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"标题",prop:"title"}},[r("el-input",{attrs:{disabled:e.formEdit,placeholder:"请输入标题"},model:{value:e.form.title,callback:function(t){e.$set(e.form,"title",t)},expression:"form.title"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"父级节点",prop:"parentId"}},[r("el-input",{attrs:{disabled:e.formEdit,placeholder:"请输入父级节点",readonly:""},model:{value:e.form.parentId,callback:function(t){e.$set(e.form,"parentId",t)},expression:"form.parentId"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"图标",prop:"icon"}},[r("el-input",{attrs:{disabled:e.formEdit,placeholder:"请输入图标"},model:{value:e.form.icon,callback:function(t){e.$set(e.form,"icon",t)},expression:"form.icon"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"资源路径",prop:"href"}},[r("el-input",{attrs:{disabled:e.formEdit,placeholder:"请输入资源路径"},model:{value:e.form.href,callback:function(t){e.$set(e.form,"href",t)},expression:"form.href"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"类型",prop:"type"}},[r("el-select",{staticClass:"filter-item",attrs:{disabled:e.formEdit,placeholder:"请输入资源请求类型"},model:{value:e.form.type,callback:function(t){e.$set(e.form,"type",t)},expression:"form.type"}},e._l(e.typeOptions,function(e){return r("el-option",{key:e.id,attrs:{label:e.labelDefault,value:e.value}})}))],1),e._v(" "),r("el-form-item",{attrs:{label:"排序",prop:"orderNum"}},[r("el-input",{attrs:{disabled:e.formEdit,placeholder:"请输入排序"},model:{value:e.form.orderNum,callback:function(t){e.$set(e.form,"orderNum",t)},expression:"form.orderNum"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"描述",prop:"description"}},[r("el-input",{attrs:{disabled:e.formEdit,placeholder:"请输入描述"},model:{value:e.form.description,callback:function(t){e.$set(e.form,"description",t)},expression:"form.description"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"前端组件",prop:"attr1"}},[r("el-input",{attrs:{disabled:e.formEdit,placeholder:"请输入描述"},model:{value:e.form.attr1,callback:function(t){e.$set(e.form,"attr1",t)},expression:"form.attr1"}})],1),e._v(" "),"update"==e.formStatus?r("el-form-item",[r("el-button",{attrs:{type:"primary"},on:{click:e.update}},[e._v("更新")]),e._v(" "),r("el-button",{on:{click:e.onCancel}},[e._v("取消")])],1):e._e(),e._v(" "),"create"==e.formStatus?r("el-form-item",[r("el-button",{attrs:{type:"primary"},on:{click:e.create}},[e._v("保存")]),e._v(" "),r("el-button",{on:{click:e.onCancel}},[e._v("取消")])],1):e._e()],1)],1),e._v(" "),r("el-card",{staticClass:"box-card",staticStyle:{"margin-top":"15px"}},[r("span",[e._v("按钮或资源")]),e._v(" "),r("menu-element",{ref:"menuElement",attrs:{menuId:e.currentId}})],1)],1)],1)],1)},staticRenderFns:[]},m=r("VU/8")(d,s,!1,null,null,null);t.default=m.exports}});