# 🌐 openvpn-telegram

OpenVPN telegram bot notifier.

## Features

- get list users connections
- kill user connection process (with the possibility of reconnection)
- received notifier if user connected to VPN server

# Installation and configuration

Configure your OpenVpn server, open the management port:

```shell
nano /etc/openvpn/server.conf
```

...added new line in end file:

```text
...
management localhost 7075
```

restarted openvpn service.

Check connection by telnet:

```shell
telnet localhost 7075
```

If connection is successful, downloaded repository

```shell
git clone https://github.com/Danil-Prog/openvpn-telegram.git
```

## Start application

Now build JAR application

```shell
gradle build
```

Parameters

| Parameters  | short | Description                                        |
|:------------|-------|----------------------------------------------------|
| --ip        | -i    | Set IP for telnet server with OpenVPN management   |
| --port      | -p    | Set port for telnet server with OpenVPN management |
| --bot-token | -bt   | Telegram bot token, used for notifier on events    |
| --chat-id   |       | Chat id telegram account administrator             |

Run app

```shell
java -jar build/libs/openvpn-telegram-{version} Release.jar --ip localhost --port 7505 -bt {token} --chat-id {id}
```

**Software work in mode Daemon, connected to OpenVPN server by telnet.**

## License

[MIT](https://choosealicense.com/licenses/mit/)
