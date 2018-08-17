/*
 * Activiti Modeler component part of the Activiti project
 * Copyright 2005-2014 Alfresco Software, Ltd. All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

/*
 * Form Properties
 */

var KisBpmFormPropertiesCtrl = [ '$scope', '$modal', '$timeout', '$translate', function($scope, $modal, $timeout, $translate) {

    // Config for the modal window
    var opts = {
        template:  'editor-app/configuration/properties/form-properties-popup.html?version=' + Date.now(),
        scope: $scope
    };

    // Open the dialog
    $modal(opts);
}];

var KisBpmFormPropertiesPopupCtrl = ['$scope', '$q', '$translate', function($scope, $q, $translate) {

    // Put json representing form properties on scope
    if ($scope.property.value !== undefined && $scope.property.value !== null
        && $scope.property.value.formProperties !== undefined
        && $scope.property.value.formProperties !== null) {
        // Note that we clone the json object rather then setting it directly,
        // this to cope with the fact that the user can click the cancel button and no changes should have happended
        $scope.formProperties = angular.copy($scope.property.value.formProperties);
    } else {
        $scope.formProperties = [];
    }

    // Array to contain selected properties (yes - we only can select one, but ng-grid isn't smart enough)
    $scope.selectedProperties = [];
    
    $scope.translationsRetrieved = false;
    
    $scope.labels = {};
    
    var idPromise = $translate('PROPERTY.FORMPROPERTIES.ID');
    var namePromise = $translate('PROPERTY.FORMPROPERTIES.NAME');
    var typePromise = $translate('PROPERTY.FORMPROPERTIES.TYPE');
    
    $q.all([idPromise, namePromise, typePromise]).then(function(results) { 
    	$scope.labels.idLabel = results[0];
        $scope.labels.nameLabel = results[1];
        $scope.labels.typeLabel = results[2];
        $scope.translationsRetrieved = true;
        
    	// Config for grid
        $scope.gridOptions = {
            data: 'formProperties',
            enableRowReordering: true,
            headerRowHeight: 28,
            multiSelect: false,
            keepLastSelected : false,
            selectedItems: $scope.selectedProperties,
            columnDefs: [{ field: 'id', displayName: $scope.labels.idLabel },
                { field: 'name', displayName: $scope.labels.nameLabel},
                { field: 'type', displayName: $scope.labels.typeLabel}]
        };
    });
    // predefined properties 
    
    var predefinedProperties = {
    		 taskUrl: {name: "审批页面URL", type: "string", required: true, description: "配置审批页面URL，通过待办任务可以打开该URL地址进行审批" },
             displayUrl: {name: "查看页面URL",  type: "string", required: true, description: "配置流程审批查看页面URL，通过已办任务，可以打开该URL地址查看流程信息" },
             detailUrl: {name: "流程查询详情页面", type: "string", required: true, description: "配置流程查询详情页面URL，通过流程查询，可以打开该URL地址查看流程详情信息。如果该参数没有配置，则默认为displayUrl配置。" },
             refuseTask: {name: "拒绝回退任务", type: "string", required: false, description: "流程任务参与决策时有效。审批拒绝时，将回退到本配置指定的流程任务节点，如果没有配置拒绝回退任务，则根据流程流转配置生成下一任务" },
             callback: {name: "回调函数类", type: "string", required: false, description: "每个配置了回调类的流程任务，系统在执行相关操作时会进行回调操作。" },
             tenantPermission: {name: "租户权限", type: "string",required: false, description: "工作流租户权限管理配置：0表示不启用；1表示启用；默认不启用。" },
             dataPermission: {name: "数据权限", type: "string",required: false, description: "" },
             orgPermission: {name: "组织权限", type: "string",required: false, description: "" },
             deptPermission: {name: "部门权限", type: "string",required: false, description: "" },
             selfPermission1: {name: "自定义权限1", type: "string",required: false, description: "" },
             selfPermission2: {name: "自定义权限2", type: "string",required: false, description: "" },
             selfPermission3: {name: "自定义权限3", type: "string",required: false, description: "" },
             selfPermission4: {name: "自定义权限4", type: "string",required: false, description: "" },
             selfPermission5: {name: "自定义权限5", type: "string",required: false, description: "" },
             retrieve: {name: "可撤回标识", type: "string",required: false, description: "" },
             voteTask: {name: "是否参与决策", type: "string",required: false, description: "" },
             votePower: {name: "特殊决策权", type: "string",required: false, description: "" },
             voteRule: {name: "投票规则", type: "string",required: false, description: "" },
             voteWeight: {name: "投票权重", type: "string",required: false, description: "" },
             voteThreshold: {name: "投票阈值", type: "string",required: false, description: "" },
             voteQuickly: {name: "是否速决", type: "string",required: false, description: "" },
             isNotify: {name: "是否发送流程任务通知", type: "string",required: false, description: "" },
             notifyKey: {name: "流程任务消息类型关键字", type: "string",required: false, description: "" },
             notifyUsersBySms: {name: "流程任务短信通知指定用户", type: "string",required: false, description: "" },
             notifyUsersByMail: {name: "流程任务邮件通知指定用户", type: "string",required: false, description: "" },
             notifyUsersByInnMsg: {name: "流程任务内部消息指定用户", type: "string",required: false, description: "" },
             notifyUsersByWechat: {name: "流程任务微信通知指定用户", type: "string",required: false, description: "" }
    };
    
    // Handler for when the value of the predefinedProperteis dropdown changes
    $scope.predefinedPropertyTypeChanged = function() {

        // Check date. If date, show date pattern
    	var selectedPreDefinedProperty = $scope.selectedProperties[0].predefinedProperty
    	if (predefinedProperties[selectedPreDefinedProperty]) {
            $scope.selectedProperties[0].id = selectedPreDefinedProperty;
            $scope.selectedProperties[0].name = predefinedProperties[selectedPreDefinedProperty].name;
            $scope.selectedProperties[0].type = predefinedProperties[selectedPreDefinedProperty].type;
        }
    };
    
    // Handler for when the value of the type dropdown changes
    $scope.propertyTypeChanged = function() {

        // Check date. If date, show date pattern
        if ($scope.selectedProperties[0].type === 'date') {
            $scope.selectedProperties[0].datePattern = 'MM-dd-yyyy hh:mm';
        } else {
            delete $scope.selectedProperties[0].datePattern;
        }

        // Check enum. If enum, show list of options
        if ($scope.selectedProperties[0].type === 'enum') {
            $scope.selectedProperties[0].enumValues = [ {value: 'value 1'}, {value: 'value 2'}];
        } else {
            delete $scope.selectedProperties[0].enumValues;
        }
    };

    // Click handler for + button after enum value
    var valueIndex = 3;
    $scope.addEnumValue = function(index) {
        $scope.selectedProperties[0].enumValues.splice(index + 1, 0, {value: 'value ' + valueIndex++});
    };

    // Click handler for - button after enum value
    $scope.removeEnumValue = function(index) {
        $scope.selectedProperties[0].enumValues.splice(index, 1);
    };

    // Click handler for add button
    var propertyIndex = 1;
    $scope.addNewProperty = function() {
        $scope.formProperties.push({ id : 'new_property_' + propertyIndex++,
            name : '',
            type : 'string',
            readable: true,
            writable: true});
    };
    
    // Add required predefined Properties
    $scope.addRequiredPredefinedProperties = function() {
        $scope.formProperties.push({ id : 'taskUrl',
            name : predefinedProperties.taskUrl.name,
            type : 'string',
            readable: true,
            writable: true});
        $scope.formProperties.push({ id : 'displayUrl',
        	name : predefinedProperties.displayUrl.name,
        	type : 'string',
        	readable: true,
        	writable: true});
        $scope.formProperties.push({ id : 'detailUrl',
        	name : predefinedProperties.detailUrl.name,
        	type : 'string',
        	readable: true,
        	writable: true});
    };

    // Click handler for remove button
    $scope.removeProperty = function() {
        if ($scope.selectedProperties.length > 0) {
            var index = $scope.formProperties.indexOf($scope.selectedProperties[0]);
            $scope.gridOptions.selectItem(index, false);
            $scope.formProperties.splice(index, 1);

            $scope.selectedProperties.length = 0;
            if (index < $scope.formProperties.length) {
                $scope.gridOptions.selectItem(index + 1, true);
            } else if ($scope.formProperties.length > 0) {
                $scope.gridOptions.selectItem(index - 1, true);
            }
        }
    };

    // Click handler for up button
    $scope.movePropertyUp = function() {
        if ($scope.selectedProperties.length > 0) {
            var index = $scope.formProperties.indexOf($scope.selectedProperties[0]);
            if (index != 0) { // If it's the first, no moving up of course
                // Reason for funny way of swapping, see https://github.com/angular-ui/ng-grid/issues/272
                var temp = $scope.formProperties[index];
                $scope.formProperties.splice(index, 1);
                $timeout(function(){
                    $scope.formProperties.splice(index + -1, 0, temp);
                }, 100);

            }
        }
    };

    // Click handler for down button
    $scope.movePropertyDown = function() {
        if ($scope.selectedProperties.length > 0) {
            var index = $scope.formProperties.indexOf($scope.selectedProperties[0]);
            if (index != $scope.formProperties.length - 1) { // If it's the last element, no moving down of course
                // Reason for funny way of swapping, see https://github.com/angular-ui/ng-grid/issues/272
                var temp = $scope.formProperties[index];
                $scope.formProperties.splice(index, 1);
                $timeout(function(){
                    $scope.formProperties.splice(index + 1, 0, temp);
                }, 100);

            }
        }
    };

    // Click handler for save button
    $scope.save = function() {

        if ($scope.formProperties.length > 0) {
            $scope.property.value = {};
            $scope.property.value.formProperties = $scope.formProperties;
        } else {
            $scope.property.value = null;
        }

        $scope.updatePropertyInModel($scope.property);
        $scope.close();
    };

    $scope.cancel = function() {
    	$scope.$hide();
    	$scope.property.mode = 'read';
    };

    // Close button handler
    $scope.close = function() {
    	$scope.$hide();
    	$scope.property.mode = 'read';
    };

}];