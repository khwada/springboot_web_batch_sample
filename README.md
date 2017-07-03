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
  + input/books.csvを取り込む
  + gradlew.bat :batch:bootRun
  + 正常終了を確認
  + もう一度webを起動してアクセスし、書籍一覧が4件になっていることを確認
  + 組み込みH2データベースは実行中はファイルロックするため、一度Webを止めてからbatchを実行

## DBをRDSへ移行
### RSDの作成
- RDSインスタンスを起動
  + MySQLを選択
  + db.t2micro
  + マルチAZ：いいえ
  + ストレージタイプ：マグネティック
  + VPCはデフォルトとする（ネットワークのセキュリティ設定は今回は考慮しない）
  + ToDo：文字コード指定
- RDSの接続確認
  + MySQL Workbench等で接続を確認

### アプリ側の接続先設定
- application-aws.ymlをのDB接続先を更新（web/batch）
- webサーバを起動
  + gradlew.bat :web:bootRun -Dspring.profiles.active=aws
