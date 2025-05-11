# 🌐 openvpn-telegram

<img height="200" src="docs/icon.png" width="200"/>

OpenVPN telegram bot notifier.

## Features

- Block user
- Unblock user
- View users connections
- Receiving notifier if user connected
- Stop user connection (with the possibility of reconnection)
- User traffic statistics(s)
- Send custom shell commands for OpenVPN server (Telnet)

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
cp .env.example .env
```

Available parameters

| Parameters             | Description                                        |
|:-----------------------|----------------------------------------------------|
| telnet.connection.host | Set IP for telnet server with OpenVPN management   |
| telnet.connection.port | Set port for telnet server with OpenVPN management |
| telegram.bot.token     | Telegram bot token, used for notifier on events    |
| telegram.bot.chat      | Chat id telegram account administrator             |
~~~~
Edit the .env file:

```shell
nano .env
```

Run app

```shell
./openvpn-monitor.sh --start
```

If you needed stop program, use:

```shell
./openvpn-monitor.sh --stop
```

All output of the program is saved in the file "openvpn-telegram.log"

**Software work in mode Daemon, connected to OpenVPN server by telnet.**

## License

[MIT](https://choosealicense.com/licenses/mit/)
