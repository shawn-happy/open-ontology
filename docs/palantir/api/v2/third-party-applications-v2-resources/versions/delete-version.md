`DELETE /api/v2/thirdPartyApplications/{thirdPartyApplicationRid}/website/versions/{versionVersion}`

Delete the Version with the specified version.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `third-party-application:deploy-application-website`.

**OAuth2 scopes**: `third-party-application:deploy-application-website`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `thirdPartyApplicationRid` | string | 是 | An RID identifying a third-party application created in Developer Console.<br>示例: `ri.third-party-applications.main.application.292db3b2-b653-4de6-971c-7e97a7b881d6` |
| `versionVersion` | string | 是 | The semantic version of the Website.<br>示例: `1.2.0` |

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `DeleteVersionPermissionDenied` | Could not delete the Version. |
