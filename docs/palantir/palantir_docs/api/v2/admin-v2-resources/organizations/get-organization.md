`GET /api/v2/admin/organizations/{organizationRid}`

Get the Organization with the specified rid.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:admin-read`.

**OAuth2 scopes**: `api:admin-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `organizationRid` | string | 是 | 示例: `ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa` |

## Response

**Organization**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `Organization` | object | 是 | 示例: `{"name":"Example Organization","host":"example.palantirfoundry.com","rid":"ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa","markingId":"18212f9a-0e63-4b79-96a0-aae04df23336"}` |
| `Organization.rid` | string | 是 | 示例: `ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa` |
| `Organization.name` | string | 是 | 示例: `Example Organization` |
| `Organization.description` | string | 否 | — |
| `Organization.markingId` | string | 是 | The ID of this Organization's underlying marking. Organization guest access can be managed<br>by updating the membership of this Marking.<br>示例: `18212f9a-0e63-4b79-96a0-aae04df23336` |
| `Organization.host` | string | 否 | The primary host name of the Organization. This should be used when constructing URLs for users of this<br>Organization.<br>示例: `example.palantirfoundry.com` |

```json
{
  "name": "Example Organization",
  "host": "example.palantirfoundry.com",
  "rid": "ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa",
  "markingId": "18212f9a-0e63-4b79-96a0-aae04df23336"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `OrganizationNotFound` | The given Organization could not be found. |
