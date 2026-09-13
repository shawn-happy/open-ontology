`GET /api/v2/thirdPartyApplications/{thirdPartyApplicationRid}/website/versions/{versionVersion}`

Get the Version with the specified version.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `third-party-application:deploy-application-website`.

**OAuth2 scopes**: `third-party-application:deploy-application-website`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `thirdPartyApplicationRid` | string | 是 | An RID identifying a third-party application created in Developer Console.<br>示例: `ri.third-party-applications.main.application.292db3b2-b653-4de6-971c-7e97a7b881d6` |
| `versionVersion` | string | 是 | The semantic version of the Website.<br>示例: `1.2.0` |

## Response

**Version**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `Version` | object | 是 | 示例: `{"version":"1.2.0"}` |
| `Version.version` | string | 是 | The semantic version of the Website.<br>示例: `1.2.0` |

```json
{
  "version": "1.2.0"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `VersionNotFound` | The given Version could not be found. |
