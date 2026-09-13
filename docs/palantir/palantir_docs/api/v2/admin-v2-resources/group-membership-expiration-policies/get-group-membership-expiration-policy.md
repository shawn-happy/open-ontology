`GET /api/v2/admin/groups/{groupId}/membershipExpirationPolicy`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Get the GroupMembershipExpirationPolicy.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:admin-read`.

**OAuth2 scopes**: `api:admin-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `groupId` | string | 是 | A Foundry Group ID.<br>示例: `0d1fe74e-2b70-4a93-9b1a-80070637788b` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Response

**GroupMembershipExpirationPolicy**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `GroupMembershipExpirationPolicy` | object | 是 | 示例: `{"maximumDuration":30,"maximumValue":"2026-01-31T00:00:00.000Z"}` |
| `GroupMembershipExpirationPolicy.maximumValue` | string | 否 | Members in this group must be added with expiration times that occur before this value.<br>示例: `2026-01-31T00:00:00.000Z` |
| `GroupMembershipExpirationPolicy.maximumDuration` | string | 否 | Members in this group must be added with expirations that are less than this duration in seconds into the future from the time they are added.<br>示例: `30` |

```json
{
  "maximumDuration": 30,
  "maximumValue": "2026-01-31T00:00:00.000Z"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `GroupMembershipExpirationPolicyNotFound` | The given GroupMembershipExpirationPolicy could not be found. |
| NOT_FOUND | `GroupNotFound` | The given Group could not be found. |
