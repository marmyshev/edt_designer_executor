# 1C:EDT Designer Executor

## Eclipse extention plugin for [1C:Enterprise Development Tools](http://1c-dn.com/1c_enterprise/1c_enterprise_developmet_tools_graphite/)

Allows to run [1C:Enterprise Designer](http://1c-dn.com/glossary/designer/) with any selected [Infobases](http://1c-dn.com/glossary/infobase/) or with [Infobases](http://1c-dn.com/glossary/infobase/) of a project.

Still under construction.

## Installation

### Necessary components

1. 1C:Enterprise Development Tools version 1.3.0 (https://releases.1c.ru/version_files?nick=DevelopmentTools10&ver=1.3.0.752)
2. Eclipse Mars2 (4.5.2) for RCP and RAP Developers (http://www.eclipse.org/downloads/packages/eclipse-rcp-and-rap-developers/mars2)
3. Java SE Development Kit 8u111 (http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

### Build with Maven


 
Will update soon.

## Usage

Start [1C:Enterprise Development Tools]().

To the toolbar added a button ![1C:Designer](https://github.com/marmyshev/edt_designer_executor/blob/master/com.marmyshev.dt.designer.executor.ui/icons/1c_designer_16x16.png?raw=true) for running a 1C:Designer


* Select one or several Infobases in __Infobase view__, then click __1C:Designer__ button to run Designers for each infobase
* For Associated Infobase(s) to your 1C:EDT project - select the project and then click __1C:Designer__ button to run all infobases or default (first) infobase.