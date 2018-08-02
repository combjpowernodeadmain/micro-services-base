webpackJsonp([49],{cgTB:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var i=a("pFYg"),r=a.n(i),n=a("Dd8w"),s=a.n(n),l=a("TG9w"),o=a("yBzI"),d=a("NYxO"),u={name:"user",components:{"depart-selector":function(){return Promise.all([a.e(0),a.e(1),a.e(4)]).then(a.bind(null,"Z8Dg"))}},data:function(){return{form:{username:void 0,name:void 0,sex:"comm_sex_man",password:void 0,description:void 0,departName:void 0,departId:void 0},rules:{name:[{required:!0,message:"请输入用户",trigger:"blur"},{min:3,max:20,message:"长度在 3 到 20 个字符",trigger:"blur"}],username:[{required:!0,message:"请输入账户",trigger:"blur"},{min:3,max:20,message:"长度在 3 到 20 个字符",trigger:"blur"}],password:[{required:!0,message:"请输入密码",trigger:"blur"},{min:5,max:20,message:"长度在 5 到 20 个字符",trigger:"blur"}],departName:[{required:!0,message:"请选择部门",trigger:"blur"}]},list:null,total:null,listLoading:!0,listQuery:{page:1,limit:20,name:void 0,username:void 0},sexOptions:[],dialogFormVisible:!1,dialogDepartVisible:!1,dialogStatus:"",userManager_btn_edit:!1,userManager_btn_del:!1,userManager_btn_add:!1,textMap:{update:"编辑",create:"创建"},tableKey:0}},created:function(){var e=this;this.getList(),this.userManager_btn_edit=this.elements["userManager:btn_edit"],this.userManager_btn_del=this.elements["userManager:btn_del"],this.userManager_btn_add=this.elements["userManager:btn_add"],Object(o.c)("comm_sex").then(function(t){e.sexOptions=t.data.rows})},computed:s()({},Object(d.b)(["elements"])),methods:{getList:function(){var e=this;this.listLoading=!0,this.listQuery.username=this.listQuery.name,Object(l.e)(this.listQuery).then(function(t){e.list=t.data.rows,e.total=t.data.total,e.listLoading=!1})},handleFilter:function(){this.getList()},handleSizeChange:function(e){this.listQuery.limit=e,this.getList()},handleCurrentChange:function(e){this.listQuery.page=e,this.getList()},handleCreate:function(){this.resetTemp(),this.dialogStatus="create",this.dialogFormVisible=!0},handleUpdate:function(e){var t=this;Object(l.d)(e.id).then(function(e){t.form=e.data;try{var a=JSON.parse(e.data.departId);"object"==(void 0===a?"undefined":r()(a))&&a?(t.form.departName=a.name,t.form.departId=a.id):(t.form.departId=e.data.departId,t.form.departName=e.data.departId)}catch(e){console.log("error：部门转化json失败"+e)}t.dialogFormVisible=!0,t.dialogStatus="update"})},handleDelete:function(e){var t=this;this.$confirm("此操作将永久删除, 是否继续?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){Object(l.c)(e.id).then(function(){t.$notify({title:"成功",message:"删除成功",type:"success",duration:2e3});var a=t.list.indexOf(e);t.list.splice(a,1)})})},handlerAddDepart:function(){this.$refs.departSelector.onSubmit()},create:function(e){var t=this;this.$refs[e].validate(function(e){if(!e)return!1;Object(l.a)(t.form).then(function(){t.dialogFormVisible=!1,t.getList(),t.$notify({title:"成功",message:"创建成功",type:"success",duration:2e3})})})},cancel:function(e){this.dialogFormVisible=!1,this.$refs[e].resetFields()},update:function(e){var t=this;this.$refs[e].validate(function(e){if(!e)return!1;t.dialogFormVisible=!1,t.form.password=void 0,Object(l.f)(t.form.id,t.form).then(function(){t.dialogFormVisible=!1,t.getList(),t.$notify({title:"成功",message:"创建成功",type:"success",duration:2e3})})})},openDepartDialog:function(){this.dialogDepartVisible=!0},closeDepartDialog:function(e){console.log(e),this.form.departName=e.label,this.form.departId=e.id,this.dialogDepartVisible=!1},resetTemp:function(){this.form={username:void 0,name:void 0,sex:"男",password:void 0,description:void 0,departId:void 0,departName:void 0}}}},c={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"app-container calendar-list-container"},[a("div",{staticClass:"filter-container"},[a("el-input",{staticClass:"filter-item",staticStyle:{width:"200px"},attrs:{placeholder:"姓名或账户"},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key))return null;e.handleFilter(t)}},model:{value:e.listQuery.name,callback:function(t){e.$set(e.listQuery,"name",t)},expression:"listQuery.name"}}),e._v(" "),a("el-button",{staticClass:"filter-item",attrs:{type:"primary",icon:"search"},on:{click:e.handleFilter}},[e._v("搜索")]),e._v(" "),e.userManager_btn_add?a("el-button",{staticClass:"filter-item",staticStyle:{"margin-left":"10px"},attrs:{type:"primary",icon:"edit"},on:{click:e.handleCreate}},[e._v("添加")]):e._e()],1),e._v(" "),a("el-table",{directives:[{name:"loading",rawName:"v-loading.body",value:e.listLoading,expression:"listLoading",modifiers:{body:!0}}],key:e.tableKey,staticStyle:{width:"100%"},attrs:{data:e.list,border:"",fit:"","highlight-current-row":""}},[a("el-table-column",{attrs:{width:"200",align:"center",label:"姓名"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("span",[e._v(e._s(t.row.name))])]}}])}),e._v(" "),a("el-table-column",{attrs:{width:"110",align:"center",label:"账户"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("span",[e._v(e._s(t.row.username))])]}}])}),e._v(" "),a("el-table-column",{attrs:{width:"110",align:"center",label:"性别"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("span",[e._v(e._s(t.row.sex))])]}}])}),e._v(" "),a("el-table-column",{attrs:{width:"110",align:"center",label:"身份"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-tag",{attrs:{type:"0"===t.row.isSuperAdmin?"primary":"warning","close-transition":""}},["0"===t.row.isSuperAdmin?a("span",[e._v("普通身份")]):"1"===t.row.isSuperAdmin?a("span",[e._v("超级管理员")]):e._e()])]}}])}),e._v(" "),a("el-table-column",{attrs:{width:"300",align:"center",label:"备注"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("span",[e._v(e._s(t.row.description))])]}}])}),e._v(" "),a("el-table-column",{attrs:{width:"180",align:"center",label:"最后时间"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("span",[e._v(e._s(t.row.updTime))])]}}])}),e._v(" "),a("el-table-column",{attrs:{width:"200",align:"center",label:"最后更新人"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("span",[e._v(e._s(t.row.updName))])]}}])}),e._v(" "),a("el-table-column",{attrs:{align:"center",label:"操作",width:"150",fixed:"right"},scopedSlots:e._u([{key:"default",fn:function(t){return[e.userManager_btn_edit?a("el-button",{attrs:{size:"small",type:"success"},on:{click:function(a){e.handleUpdate(t.row)}}},[e._v("编辑\n        ")]):e._e(),e._v(" "),e.userManager_btn_del?a("el-button",{attrs:{size:"small",type:"danger"},on:{click:function(a){e.handleDelete(t.row)}}},[e._v("删除\n        ")]):e._e()]}}])})],1),e._v(" "),a("div",{directives:[{name:"show",rawName:"v-show",value:!e.listLoading,expression:"!listLoading"}],staticClass:"pagination-container"},[a("el-pagination",{attrs:{"current-page":e.listQuery.page,"page-sizes":[10,20,30,50],"page-size":e.listQuery.limit,layout:"total, sizes, prev, pager, next, jumper",total:e.total},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange,"update:currentPage":function(t){e.$set(e.listQuery,"page",t)}}})],1),e._v(" "),a("el-dialog",{attrs:{title:e.textMap[e.dialogStatus],visible:e.dialogFormVisible},on:{"update:visible":function(t){e.dialogFormVisible=t}}},[a("el-form",{ref:"form",attrs:{model:e.form,rules:e.rules,"label-width":"100px"}},[a("el-form-item",{attrs:{label:"姓名",prop:"name"}},[a("el-input",{attrs:{placeholder:"请输入姓名"},model:{value:e.form.name,callback:function(t){e.$set(e.form,"name",t)},expression:"form.name"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"账户",prop:"username"}},["create"==e.dialogStatus?a("el-input",{attrs:{placeholder:"请输入账户"},model:{value:e.form.username,callback:function(t){e.$set(e.form,"username",t)},expression:"form.username"}}):a("el-input",{attrs:{placeholder:"请输入账户",readonly:""},model:{value:e.form.username,callback:function(t){e.$set(e.form,"username",t)},expression:"form.username"}})],1),e._v(" "),"create"==e.dialogStatus?a("el-form-item",{attrs:{label:"密码",placeholder:"请输入密码",prop:"password"}},[a("el-input",{attrs:{type:"password"},model:{value:e.form.password,callback:function(t){e.$set(e.form,"password",t)},expression:"form.password"}})],1):e._e(),e._v(" "),a("el-form-item",{attrs:{label:"部门",placeholder:"请选择部门",prop:"departName"}},[a("el-input",{attrs:{readonly:"",type:"text"},model:{value:e.form.departName,callback:function(t){e.$set(e.form,"departName",t)},expression:"form.departName"}},[a("el-button",{attrs:{slot:"append",icon:"el-icon-search"},on:{click:function(t){e.openDepartDialog()}},slot:"append"})],1)],1),e._v(" "),a("el-form-item",{attrs:{label:"性别"}},[a("el-select",{staticClass:"filter-item",attrs:{placeholder:"请选择"},model:{value:e.form.sex,callback:function(t){e.$set(e.form,"sex",t)},expression:"form.sex"}},e._l(e.sexOptions,function(e){return a("el-option",{key:e.id,attrs:{label:e.labelDefault,value:e.value}})}))],1),e._v(" "),a("el-form-item",{attrs:{label:"描述"}},[a("el-input",{attrs:{type:"textarea",autosize:{minRows:3,maxRows:5},placeholder:"请输入内容"},model:{value:e.form.description,callback:function(t){e.$set(e.form,"description",t)},expression:"form.description"}})],1)],1),e._v(" "),a("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(t){e.cancel("form")}}},[e._v("取 消")]),e._v(" "),"create"==e.dialogStatus?a("el-button",{attrs:{type:"primary"},on:{click:function(t){e.create("form")}}},[e._v("确 定")]):a("el-button",{attrs:{type:"primary"},on:{click:function(t){e.update("form")}}},[e._v("确 定")])],1)],1),e._v(" "),a("el-dialog",{attrs:{title:"选择部门",width:"30%",visible:e.dialogDepartVisible},on:{"update:visible":function(t){e.dialogDepartVisible=t}}},[a("depart-selector",{ref:"departSelector",on:{closeDepartDialog:e.closeDepartDialog}}),e._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{staticClass:"filter-item",staticStyle:{"margin-left":"10px"},attrs:{type:"primary",icon:"edit"},on:{click:e.handlerAddDepart}},[e._v("添加")])],1)],1)],1)},staticRenderFns:[]},m=a("VU/8")(u,c,!1,null,null,null);t.default=m.exports}});