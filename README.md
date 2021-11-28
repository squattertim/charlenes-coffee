# Charlene's Coffee Corner

1. Build with: `mvn clean install`
2. Run with: `java -jar target/charlenes-coffee-1.0-SNAPSHOT.jar`
3. Make order by sentence with following grammar

```
<order> = order <order items> [bonus code <bonus code> <date of last order>];
<order items> = <order item> | <order item> (and <order item>)*;
<order item> = <number> <item> | <number> <item> with <extra>;
```