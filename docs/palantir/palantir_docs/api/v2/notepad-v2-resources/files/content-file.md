`GET /api/v2/notepad/files/{fileRid}/content`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Download file content.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:notepad-export`.

**OAuth2 scopes**: `api:notepad-export`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `fileRid` | string | 是 | The unique identifier for a File<br>示例: `ri.notepad.main.file.cf32c039-353c-4555-9704-eacfdfaa2c1c` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Response

**body**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `body` | string | 是 | — |

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `ContentFilePermissionDenied` | Could not content the File. |
