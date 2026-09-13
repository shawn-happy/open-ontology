`POST /api/v2/admin/markings/parseClassifications`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Parses classification marking strings (e.g. 'S//NF') into their component marking IDs. Strings that cannot be parsed are returned in 'errors' with a human-readable message.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:admin-read`.

**OAuth2 scopes**: `api:admin-read`

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Request body

```json
{
  "classificationStrings": [
    "MTS//MNF",
    "INVALID_MARKING"
  ]
}
```

## Response

**ParseClassificationsResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ParseClassificationsResponse` | object | 是 | 示例: `{"parsed":{"MTS//MNF":["MNF","MTS"]},"errors":{"INVALID_MARKING":"Unknown marking: INVALID_MARKING"}}` |
| `ParseClassificationsResponse.parsed` | map | 否 | Map of valid classification strings to their component marking IDs. Strings that could not be parsed are absent from this map and appear in 'errors' instead.<br>示例: `{"MTS//MNF":["MNF","MTS"]}` |
| `ParseClassificationsResponse.parsed.array` | list<MarkingId> | 是 | — |
| `ParseClassificationsResponse.parsed.array.MarkingId` | string | 是 | The ID of a security marking. |
| `ParseClassificationsResponse.errors` | map | 否 | Map of classification strings that could not be parsed to a human-readable error message.<br>示例: `{"INVALID_MARKING":"Unknown marking: INVALID_MARKING"}` |

```json
{
  "parsed": {
    "MTS//MNF": [
      "MNF",
      "MTS"
    ]
  },
  "errors": {
    "INVALID_MARKING": "Unknown marking: INVALID_MARKING"
  }
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `ParseClassificationsPermissionDenied` | The provided token does not have permission to parse the given classification strings. |
| INVALID_ARGUMENT | `CbacUnavailable` | CBAC is not available. |
