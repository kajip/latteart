# LatteArt

## これは何？

  * ローカルで動作するJavaVMのステータスを採取するツールです。
  * ステータス取得には、[JMX(Java Management eXtentions)](http://www.oracle.com/technetwork/articles/java/javamanagement-140525.html)を利用します。
  * 取得するステータスは、JSONで記述する設定ファイルで指定します。
  * 採取したステータスは、[LTSVフォーマット](http://ltsv.org)でログに出力します。
  * プラグイン形式で出力先を追加でき~~ます。~~るようになる予定

## 使い方

  * [インストール媒体](https://github.com/tkajita/java-monitor/releases/latest)をダウンロードし、解凍します
  * 解凍したディレクトリに移動します
  * conf/monitor.json ファイルを修正し、採取情報を指定します
  * 下記コマンドを実行します。<pid> は、観測対象JavaVMのプロセスIDです
  
  ```bin/latteart <pid>```

### コマンドオプション

  * -h ヘルプメッセージを表示
  * -l MBeanのObjectNameの一覧表示
  * -a <objectName> 取得可能な属性の一覧表示
  * -c <url> 指定したURLから設定ファイルを読み込む。ローカルファイル、HTTP(S)が利用できる（はず）
  * -i <interval> MBeanを取得する間隔を秒で指定

## conf/monitor.json ファイル

  * jsonフォーマットの設定ファイルです
  * 採取間隔と採取対象を指定します
  * C++ や Java スタイルのコメントが使えます( /* */、// など）
  * シェルスクリプトスタイル（行の先頭に #）も使えます

### 例。5分間隔でメモリ情報を取得する

```json
{
  "queries" : [
    // Memory Pool
    {
      "query" : "java.lang:type=MemoryPool,*",
      "attributeNames" : [
        "Usage"
      ]
    }
  ]
}
```

#### queries 属性

  * 採取するMBeanを指定します。
  * 後述する Query Object のリストになっており、MBeanは複数指定可能です。
  * 全ての情報は、一つのファイルにまとめて出力されます。

### Query Object

  * queries の子属性です。
  * 対象MBeanのObjectNameと採取する属性を指定します
  * query、attributes の２つの属性を持ちます

#### query 属性

  * JMXのObjectNameフォーマットでクエリーを指定します
  * 詳細は、[こちら](https://docs.oracle.com/javase/8/docs/api/javax/management/ObjectName.html)

#### attributes 属性

  * 採取する情報の属性を文字列のリストで指定します

## FAQ

  * プロセスID はどうやって調べるといいのかな？

    JDK に標準で付属する jpsコマンドを使うとローカルで稼働しているJavaVMのプロセスID一覧が取得できます。

  * JMXの属性はどうやって調べるといいのかな？
  
    ~~JDK に標準で付属する jconsoleというGUIツールを使うと JMXのツリーにアクセスできます。~~

    -  -l で取得可能なMBeanのObjectName一覧が表示できるようになりました。

    -  -a objectName で取得可能な属性一覧が表示できるようになりました。

  * ファイルの出力先を変えたい。

    etc/logback.groovy で出力先を指定できます

  * MBean毎に採取間隔を変えたい。
  
    Bean毎にmonitor.json を用意し、MBean毎に LatteArt を起動します。
    読込む monitor.json は、-c オプションを使って指定します。このとき、ファイル名は URL形式で指定します。

    ```
    latteart -c file:///etc/monitor-memory.json 1234
    ```