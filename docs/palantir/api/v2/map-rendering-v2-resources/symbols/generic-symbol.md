`GET /api/v2/mapRendering/symbols/{symbolId}/generic`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:map-read`.

**OAuth2 scopes**: `api:map-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `symbolId` | string | 是 | Unique identifier for a symbol that can be used to fetch the symbol as a PNG using loadGenericSymbol endpoint.<br>The ID is opaque and not meant to be parsed in any way.<br>示例: `temp` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `size` | integer | 是 | 示例: `1` |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Response

**body**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `body` | string | 是 | — |

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `GenericSymbolPermissionDenied` | Could not generic the Symbol. |
