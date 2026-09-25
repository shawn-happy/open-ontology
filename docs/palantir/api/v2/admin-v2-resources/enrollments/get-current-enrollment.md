`GET /api/v2/admin/enrollments/getCurrent`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Returns the Enrollment associated with the current User's primary organization.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:admin-read`.

**OAuth2 scopes**: `api:admin-read`

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Response

**Enrollment**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `Enrollment` | object | 是 | 示例: `{"name":"Example Enrollment","createdTime":"2003-05-06T12:34:56.789Z","rid":"ri.control-panel.main.customer.466f812b-f974-4478-9d4f-90402cd3def6"}` |
| `Enrollment.rid` | string | 是 | 示例: `ri.control-panel.main.customer.466f812b-f974-4478-9d4f-90402cd3def6` |
| `Enrollment.name` | string | 是 | 示例: `Example Enrollment` |
| `Enrollment.createdTime` | string | 否 | The time at which the resource was created.<br>示例: `2003-05-06T12:34:56.789Z` |

```json
{
  "name": "Example Enrollment",
  "createdTime": "2003-05-06T12:34:56.789Z",
  "rid": "ri.control-panel.main.customer.466f812b-f974-4478-9d4f-90402cd3def6"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `GetCurrentEnrollmentPermissionDenied` | Could not getCurrent the Enrollment. |
| NOT_FOUND | `EnrollmentNotFound` | The given Enrollment could not be found. |
