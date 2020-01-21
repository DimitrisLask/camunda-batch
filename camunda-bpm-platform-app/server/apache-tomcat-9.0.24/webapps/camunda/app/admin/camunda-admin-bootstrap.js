/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

window.__define("camunda-admin-bootstrap",["./scripts/camunda-admin-ui"],function(){"use strict";var a=window.CamundaAdminUi;requirejs.config({baseUrl:"../../../lib"});var r=window;a.exposePackages(r),window.define=window.__define,window.require=window.__require,requirejs(["globalize"],function(e){e(requirejs,["angular","camunda-commons-ui","camunda-bpm-sdk-js","jquery"],r);var i=window.PLUGIN_PACKAGES||[],o=window.PLUGIN_DEPENDENCIES||[];i.forEach(function(e){var i=document.createElement("link");i.setAttribute("rel","stylesheet"),i.setAttribute("href",e.location+"/plugin.css"),document.head.appendChild(i)}),requirejs.config({packages:i,baseUrl:"../",paths:{ngDefine:"../../lib/ngDefine"}});var n=["angular","ngDefine"].concat(o.map(function(e){return e.requirePackageName}));requirejs(n,function(e){if(window.camAdminConf&&window.camAdminConf.csrfCookieName&&e.module("cam.commons").config(["$httpProvider",function(e){e.defaults.xsrfCookieName=window.camAdminConf.csrfCookieName}]),void 0!==window.camAdminConf&&window.camAdminConf.customScripts){var i=window.camAdminConf.customScripts||{},n={};["baseUrl","paths","bundles","shim","map","config","packages","waitSeconds","context","callback","enforceDefine","xhtml","urlArgs","scriptType"].forEach(function(e){i[e]&&(n[e]=i[e])}),requirejs.config(n),requirejs(i.deps||[],function(){e.module("cam.admin.custom",i.ngDeps),window.define=void 0,window.require=void 0,a(o)})}else e.module("cam.admin.custom",[]),require([],function(){window.define=void 0,window.require=void 0,a(o)})})})}),requirejs(["camunda-admin-bootstrap"],function(){});