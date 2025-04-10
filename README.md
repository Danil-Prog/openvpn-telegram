# 🌐 openvpn-telegram

<img height="200" src="docs/icon.png" width="200"/>

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

| Parameters               | Description                                        |
|:-------------------------|----------------------------------------------------|
| --telnet.connection.host | Set IP for telnet server with OpenVPN management   |
| --telnet.connection.port | Set port for telnet server with OpenVPN management |
| --telegram.bot.token     | Telegram bot token, used for notifier on events    |
| --telegram.bot.chat      | Chat id telegram account administrator             |

Run app

```shell
gradle bootRun --args='--telnet.connection.host={localhost} --telnet.connection.port={7505} --telegram.bot.token={token} --telegram.bot.chat={chatId}'
```

**Software work in mode Daemon, connected to OpenVPN server by telnet.**

## License

[MIT](https://choosealicense.com/licenses/mit/)
