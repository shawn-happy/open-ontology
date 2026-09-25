`POST /api/v2/thirdPartyApplications/{thirdPartyApplicationRid}/website/deploy`

Deploy a version of the Website.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `third-party-application:deploy-application-website`.

**OAuth2 scopes**: `third-party-application:deploy-application-website`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `thirdPartyApplicationRid` | string | 是 | An RID identifying a third-party application created in Developer Console.<br>示例: `ri.third-party-applications.main.application.292db3b2-b653-4de6-971c-7e97a7b881d6` |

## Request body

```json
{
  "version": "1.2.0"
}
```

## Response

**Website**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `Website` | object | 是 | 示例: `{"subdomains":["myapp.example.com"],"deployedVersion":"1.2.0"}` |
| `Website.deployedVersion` | string | 否 | The version of the Website that is currently deployed. |
| `Website.subdomains` | list<Subdomain> | 否 | The subdomains from which the Website is currently served. |
| `Website.subdomains.Subdomain` | string | 是 | A subdomain from which a website is served. |

```json
{
  "subdomains": [
    "myapp.example.com"
  ],
  "deployedVersion": "1.2.0"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `DeployWebsitePermissionDenied` | Could not deploy the Website. |
