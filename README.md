# gitlab-release-reminder
A reminder to look back on the release.

## Usage
```$xslt
$ docker run --rm -e ${TIMEZONE} -v `pwd`:/root sakebook/gitlab-release-reminder ${CONFIG_FILE}
```

### Supported config file
- JSON
- XML
- YAML
- HOCON
- TOML
- properties

```$xslt
$ docker run --rm -e TZ=Asia/Tokyo -v `pwd`:/root sakebook/gitlab-release-reminder config.toml
```

provided by [konf](https://github.com/uchuhimo/konf)

### Supported chat tool
- Slack

### config

- API

|name|required|description|
|:---:|:---:|:---:|
|host|Yes|GitLab host|
|token|No|GitLab API token|

- Projects

|name|required|description|
|:---:|:---:|:---:|
|id|Yes|Project ID|
|note|No|Release note link|
|remindingDay|No|remind span|
|iconUrl|No|thumbnail|

- Slack

|name|required|description|
|:---:|:---:|:---:|
|webhook|Yes|Webhook url|
|mention|Yes|mention|

## Build

```$xslt
$ ./gradlew jibDockerBuild
```