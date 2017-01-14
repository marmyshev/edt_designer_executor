# 1C:EDT Designer Executor [![Build Status](https://travis-ci.org/marmyshev/edt_designer_executor.svg?branch=master)](https://travis-ci.org/marmyshev/edt_designer_executor)

## Eclipse extension plugin for [1C:Enterprise Development Tools](http://1c-dn.com/1c_enterprise/1c_enterprise_developmet_tools_graphite/)

Allows to run [1C:Enterprise Designer](http://1c-dn.com/glossary/designer/) with any selected [Infobases](http://1c-dn.com/glossary/infobase/) or with [Infobases](http://1c-dn.com/glossary/infobase/) of a project.

## Installation

Add Eclipse Update Site to your 1C:EDT installation: [https://marmyshev.github.io/updates/edt/designer_executor](https://marmyshev.github.io/updates/edt/designer_executor) and install the plugin.

### Necessary components

1. 1C:Enterprise Development Tools version 1.4.0 (http://1c-dn.com/1c_enterprise/1c_enterprise_developmet_tools_graphite/)
2. Eclipse Mars2 (4.5.2) for RCP and RAP Developers (http://www.eclipse.org/downloads/packages/eclipse-rcp-and-rap-developers/mars2)
3. Java SE Development Kit 8u111 (http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

### Build with Maven

Before all, you need to install Maven form (https://maven.apache.org/download.cgi) with instructions (https://maven.apache.org/install.html) and set encrypted info for user of (https://partners.v8.1c.ru) in settings.xml [follow here to get example](https://github.com/1C-Company/dt-example-plugins/tree/master/simple-plugin).

1. Open console in plugin directory
2. Execute command:
```
mvn clean verify -f build/pom.xml
```
3. Compiled p2 repository is in plugin subfolder:
```
/..PathToPluginDirectory../repository/target/repository/
```
 

## Usage

Start [1C:Enterprise Development Tools](http://1c-dn.com/1c_enterprise/1c_enterprise_developmet_tools_graphite/).

Able to launch Designer from standard launch configs. To the toolbar added a button ![1C:Designer](https://github.com/marmyshev/edt_designer_executor/blob/master/com.marmyshev.dt.designer.executor.ui/icons/1c_designer_16x16.png?raw=true) for running a 1C:Designer directly.


* Use shurtcut Alt+Shift+D D to run Designer in "Debug" mode or Alt+Shift+X D in "Run" mode of launching configs.
* Select one or several Infobases in __Infobase view__, then click in context menu __Start 1C:Designer__ to run Designers for each infobase.
* For Associated Infobase(s) to your 1C:EDT project - select the project and then click __1C:Designer__ button to run all infobases or default (first) infobase.

# 1С:EDT Запуск Конфигуратора

## Плагин расширения для Eclipse [1C:Enterprise Development Tools](http://v8.1c.ru/overview/release_IDE_beta/)

Позволяет запускать [1С:Конфигуратор](http://v8.1c.ru/overview/Term_000000008.htm) в любых выбранных  [Информационных базах](http://v8.1c.ru/overview/Term_000000641.htm) или в [Информационных базах](http://v8.1c.ru/overview/Term_000000641.htm) привязанных к проекту.

## Установка

Добавьте репозиторий обновлений Eclipse Update Site в установленную 1C:EDT: [https://marmyshev.github.io/updates/edt/designer_executor](https://marmyshev.github.io/updates/edt/designer_executor) и установите из репозитория плагин.

### Необходимые компоненты

1. 1C:Enterprise Development Tools версии 1.4.0 (https://releases.1c.ru/version_files?nick=DevelopmentTools10&ver=1.4.0.699)
2. Eclipse Mars2 (4.5.2) for RCP and RAP Developers (http://www.eclipse.org/downloads/packages/eclipse-rcp-and-rap-developers/mars2)
3. Java SE Development Kit 8u111 (http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

### Сборка с Maven

Сначала необходимо установить сам Maven отсюда (https://maven.apache.org/download.cgi) по инструкции отсюда (https://maven.apache.org/install.html) и задать зашифрованный пароль для пользователя (https://partners.v8.1c.ru) в файле settings.xml [см. пример здесь](https://github.com/1C-Company/dt-example-plugins/tree/master/simple-plugin).

1. Откройте консоль в папке плагина
2. Выполните команду:
```
mvn clean verify -f build/pom.xml
```
3. Собранный p2 репозиторий находится в подпапке:
```
/..ПутьКПапкеПлагина../repository/target/repository/
```
 
## Использование

Запустите [1C:Enterprise Development Tools](http://v8.1c.ru/overview/release_IDE_beta/).

Возможно запустить Конфигуратор из стандартных настроке запуска. На панель инструментов добавлена кнопка ![1C:Designer](https://github.com/marmyshev/edt_designer_executor/blob/master/com.marmyshev.dt.designer.executor.ui/icons/1c_designer_16x16.png?raw=true) позволяющая запускать 1С:Конфигуратор непосредственно.


* Нажмите Alt+Shift+D D для старта Конфигуратора в режиме "Отладки" или Alt+Shift+X D в режиме "Запуск" из конфигураций запуска.
* Выделите одну или несколько информационных баз в списке __Infobase view__, затем кликните в контектном меню __Запустить 1С:Конфигуратор__ чтобы запустить конфигуратор в каждой информационной базе.
* Для информационных баз, привязанных к вашему 1С:EDT проекту, выделите проект и кликните кнопку __1С:Конфигуратор__ чтобы запустить все или ИБ по умолчанию (первую).
