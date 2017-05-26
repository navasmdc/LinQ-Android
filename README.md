# LinQ for Android / Java

Are you tired of search items in your list making a lot of for sentences? Or you want to delete some items or update an exactly value from an item?

#### LinQ is your solution!

With this library you will can execute SQL sentences over an Object List.


## Add library
```xml
repositories {
    jcenter()
    maven { url 'https://dl.bintray.com/navasmdc/maven' }
}

dependencies {
    compile 'com.github.navasmdc:LinQ:1.0@aar'
}

```

Or add <a href="https://raw.githubusercontent.com/navasmdc/LinQ-Android/master/release/linq-1.0.jar">
JAR
</a> to your project

## How to use
For init a new sentence you have to make a new intance from `new LinQ()` andd cal to `FROM(list)`

> You can test the all examples in
> <a href="https://github.com/navasmdc/LinQ-Android/tree/master/demolinq/src/test/java/com/gc/demolinq">
Tests folder
</a>

### Android interactive demo soon!

##### Example list
```java

{id=0, name='Name0', locale={language='ES', country='es'}}
{id=1, name='Name1', locale={language='ES', country='es'}}
{id=2, name='Name2', locale={language='ES', country='us'}}
{id=3, name='Name3', locale={language='EN', country='en'}}
{id=4, name='Name4', locale={language='EN', country='us'}}
{id=5, name='Name5', locale={language='ES', country='es'}}
{id=6, name='Name6', locale={language='EN', country='gb'}}
{id=7, name='Name7', locale={language='ES', country='es'}}
{id=8, name='Name8', locale={language='FR', country='ca'}}
{id=9, name='Name9', locale={language='FR', country='fr'}}
{id=10, name='Name10', locale={language='ES', country='es'}}

```

### Select

This sentence ables to select one or some items from the list or only specific fields.

##### Select field

```java
List<Object> result = new LinQ<User>()
	.FROM(users)
	.SELECT("name")
	.exec();
	
----------------------------------------------------------------
Name0
Name1
Name2
Name3
Name4
Name5
Name6
Name7
Name8
Name9
Name10
----------------------------------------------------------------


```
#### WHERE

WHERE ables to condition your search.
>Comparators = != < <= > >= CONTAINS (only Strings)

##### Simple
```java

List<User> result =new LinQ<User>()
	.FROM(users)
	.SELECT()
	.WHERE("id < 5")
	.exec()
	
----------------------------------------------------------------
{id=0, name='Name0', locale={language='ES', country='es'}}
{id=1, name='Name1', locale={language='ES', country='es'}}
{id=2, name='Name2', locale={language='ES', country='us'}}
{id=3, name='Name3', locale={language='EN', country='en'}}
{id=4, name='Name4', locale={language='EN', country='us'}}
----------------------------------------------------------------


```
##### Field Select
```java

List<Object> result = new LinQ<User>()
	.FROM(users)
	.SELECT("name")
	.WHERE("id = 5")
	.exec();
	
----------------------------------------------------------------
Name5
----------------------------------------------------------------


```
##### Multy Field Select
```java

List<HashMap<String, Object>> result = new LinQ<User>()
	.FROM(users)
	.SELECT("name","locale")
	.WHERE("id = 4")
	.exec();
	
----------------------------------------------------------------
{name=Name4, locale={language='EN', country='us'}}
----------------------------------------------------------------


```
##### AND Select
```java

List<User> result = new LinQ<User>()
	.FROM(users)
	.SELECT()
	.WHERE("id > 1 AND id < 4")
	.exec();
	
----------------------------------------------------------------
{id=2, name='Name2', locale={language='ES', country='us'}}
{id=3, name='Name3', locale={language='EN', country='en'}}
----------------------------------------------------------------


```
##### OR Select
```java

List<User> result = new LinQ<User>()
	.FROM(users)
	.SELECT()
	.WHERE("id > 1 AND id < 4 OR id > 6 AND id < 9")
	.exec();
	
----------------------------------------------------------------
Name5
----------------------------------------------------------------


```
##### Contains Select
```java

List<User> result = new LinQ<User>()
	.FROM(users)
	.SELECT()
	.WHERE("name CONTAINS 1")
	.exec();
	
----------------------------------------------------------------
{id=1, name='Name1', locale={language='ES', country='es'}}
{id=10, name='Name10', locale={language='ES', country='es'}}
----------------------------------------------------------------


```
##### Params Select
```java

List<User> result = new LinQ<User>()
	.FROM(users)
	.SELECT()
	.WHERE("id < {0}",7)
	.exec();
	
----------------------------------------------------------------
{id=0, name='Name0', locale={language='ES', country='es'}}
{id=1, name='Name1', locale={language='ES', country='es'}}
{id=2, name='Name2', locale={language='ES', country='us'}}
{id=3, name='Name3', locale={language='EN', country='en'}}
{id=4, name='Name4', locale={language='EN', country='us'}}
{id=5, name='Name5', locale={language='ES', country='es'}}
{id=6, name='Name6', locale={language='EN', country='gb'}}
----------------------------------------------------------------


```
##### Internal Object Select
```java

List<User> result = new LinQ<User>()
	.FROM(users)
	.SELECT()
	.WHERE("locale.country = {0}","es")
	.exec();
	
----------------------------------------------------------------
{id=0, name='Name0', locale={language='ES', country='es'}}
{id=1, name='Name1', locale={language='ES', country='es'}}
{id=5, name='Name5', locale={language='ES', country='es'}}
{id=7, name='Name7', locale={language='ES', country='es'}}
{id=10, name='Name10', locale={language='ES', country='es'}}
----------------------------------------------------------------


```
### Delete

This sentence ables to remove one or some items from the list throught a WHERE condition.

> Exec return a list with the afected items

##### Delete example
```java

List<User> result = new LinQ<User>()
	.FROM(copyUsers)
	.DELETE()
	.WHERE("id > 0")
	.exec();
	
----------------------------------------------------------------
{id=0, name='Name0', locale={language='ES', country='es'}}
----------------------------------------------------------------


```
### Update

This sentence ables to update the value of one or some items from the list throught a WHERE condition.

> Exec return a list with the afected items

##### Update example
```java

List<User> result = new LinQ<User>()
	.FROM(copyUsers)
	.UPDATE("locale.country=modified")
	.WHERE("id < 5").exec();	
----------------------------------------------------------------
{id=0, name='Name0', locale={language='ES', country='modified'}}
{id=1, name='Name1', locale={language='ES', country='modified'}}
{id=2, name='Name2', locale={language='ES', country='modified'}}
{id=3, name='Name3', locale={language='EN', country='modified'}}
{id=4, name='Name4', locale={language='EN', country='modified'}}
{id=5, name='Name5', locale={language='ES', country='es'}}
{id=6, name='Name6', locale={language='EN', country='gb'}}
{id=7, name='Name7', locale={language='ES', country='es'}}
{id=8, name='Name8', locale={language='FR', country='ca'}}
{id=9, name='Name9', locale={language='FR', country='fr'}}
{id=10, name='Name10', locale={language='ES', country='es'}}
----------------------------------------------------------------

List<User> result = new LinQ<User>()
	.FROM(copyUsers)
	.UPDATE("locale={0}, name = ITALIAN",new Locale("IT","it"))
	.WHERE("id < 5 OR id > 8")
	.exec();
----------------------------------------------------------------
{id=0, name=' ITALIAN', locale={language='IT', country='it'}}
{id=1, name=' ITALIAN', locale={language='IT', country='it'}}
{id=2, name=' ITALIAN', locale={language='IT', country='it'}}
{id=3, name=' ITALIAN', locale={language='IT', country='it'}}
{id=4, name=' ITALIAN', locale={language='IT', country='it'}}
{id=5, name='Name5', locale={language='ES', country='es'}}
{id=6, name='Name6', locale={language='EN', country='gb'}}
{id=7, name='Name7', locale={language='ES', country='es'}}
{id=8, name='Name8', locale={language='FR', country='ca'}}
{id=9, name=' ITALIAN', locale={language='IT', country='it'}}
{id=10, name=' ITALIAN', locale={language='IT', country='it'}}----------------------------------------------------------------

```
