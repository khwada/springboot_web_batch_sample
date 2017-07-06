SpringBoot web & batchサンプル
====

SpringBootアプリをAWSで動かすまでのサンプル

## ローカル実行
- DB: 組み込みH2データベース
- webの動作確認
  + gradlew.bat :web:bootRun
  + 初回起動時にflywayでddl/dmlが流れる（DBファイルは、/db内に生成される）
  + http://localhost:8080 にアクセスして書籍一覧が２件表示されることを確認
  + webアプリを終了（Ctrl + C）
- batchの動作確認
  + バッチを実行すると、input/books.csvを取り込む
  + gradlew.bat :batch:bootRun
  + 正常終了を確認
  + もう一度webを起動してブラウザアクセスし、書籍一覧が4件になっていることを確認
  + ※組み込みH2データベースは実行中はファイルロックするため、一度Webを止めてからbatchを実行

## DBをRDSへ移行
### RSDの作成
- パラメータグループを作成
  + mysql5.6
- 作成したパラメータグループを編集（文字コード指定のため）
  + character_set_client: utf8
  + character_set_connection: utf8
  + character_set_database: utf8
  + character_set_results: utf8
  + character_set_server: utf-8
  + skip-character-set-client-handshake: 1
- RDSインスタンスを起動
  + MySQLを選択
  + db.t2micro
  + マルチAZ：いいえ
  + ストレージタイプ：マグネティック
  + VPCはデフォルトとする（ネットワークのセキュリティ設定は今回は考慮しない）
  + DB パラメータグループ：上記で作成したパラメータグループを指定
- RDSの接続確認
  + MySQL Workbench等で接続を確認

### アプリ側の接続先設定
- application-aws.ymlをのDB接続先を更新（web/batch）
- webアプリを起動
  + gradlew.bat :web:bootRun -Dspring.profiles.active=aws
  + ※Spring BootのProfile機能で読み込む設定ファイルを切り替えて、接続先DBをRDSに切替
  + http://localhost:8080 にアクセスして、書籍一覧が２件表示されることを確認
- batchを実行
  + gradlew.bat :batch:bootRun -Dspring.profiles.active=aws
  + ※今回はRDSに繋ぎにいっているので、webアプリの停止は不要
  + ブラウザ画面を更新して、書籍一覧が4件表示されることを確認

## WebサーバをEC2に移行
### EC2インスタンスを作成
- EC2インスタンスを作成
  + Amazon Linux
  + t2.micro
- PuTTYgenなどでpemファイルからppkファイルを作成しておく
  + 参考： http://www.machiiro.jp/2014/04/18/aws_puttygen/
- セキュリティグループの設定
  + インバウンドの設定で、ポート8080をオープンしておく
- RDSのセキュリティグループで、ポート3306に対するソースを任意の場所に変更（本来は望ましくない設定だが、ネットワークのセキュリティ設定は今回は考慮しない）

### EC2へのミドルウェアインストール
- openjdk8をインストール
  + sudo yum -y install java-1.8.0-openjdk-devel
  + sudo alternatives --config java

### EC2へのデプロイ
- web/batchそれぞれのモジュールをローカルでbuild
  + gradlew.bat :web:buil
  + gradlew.bat :batch:build
  + web/batchそれぞれのディレクトリのbuild/libsにjarが生成される
- EC2上に配置
  + SCPでbuild成果物およびbatch入力ファイルをアップロード（上記で作成したppkファイルを使用）
```
├── batch.jar
├── input
│   └── books.csv
└── web.ja
```

### EC2でのアプリ起動＆動作確認
- webアプリの起動（バックグラウンド実行）
  + java -jar -Dspring.profiles.active=aws web.jar &
  + http://{EC2のpublic DNS}:8080 にアクセス
  + 書籍一覧が4件表示されることを確認（上記でローカルから実行したbatch処理の結果が取り込まれているため）
  + Ctrl + Cで抜ける
- batch実行
  + java -jar -Dspring.profiles.active=aws batch.jar
  + ブラウザを更新し、書籍一覧が6件表示されることを確認
