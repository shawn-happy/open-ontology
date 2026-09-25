`GET /api/v2/audit/organizations/{organizationRid}/logFiles/{logFileId}/content`

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:audit-read`.

**OAuth2 scopes**: `api:audit-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `organizationRid` | string | 是 | 示例: `ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa` |
| `logFileId` | string | 是 | The ID of an audit log file<br>示例: `S2VlcEV4cGxvcmluZw==` |

## Response

**body**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `body` | string | 是 | — |

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `GetLogFileContentPermissionDenied` | Could not content the LogFile. |
