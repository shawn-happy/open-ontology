`PUT /api/v2/admin/groups/{groupId}/providerInfo`

Replace the GroupProviderInfo.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:admin-write`.

**OAuth2 scopes**: `api:admin-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `groupId` | string | 是 | A Foundry Group ID.<br>示例: `0d1fe74e-2b70-4a93-9b1a-80070637788b` |

## Request body

```json
{
  "providerId": "2838c8f3-d76a-4e99-acf1-1dee537e4c48"
}
```

## Response

**GroupProviderInfo**

The replaced GroupProviderInfo

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `GroupProviderInfo` | object | 是 | The replaced GroupProviderInfo<br>示例: `{"providerId":"2838c8f3-d76a-4e99-acf1-1dee537e4c48"}` |
| `GroupProviderInfo.providerId` | string | 是 | The ID of the Group in the external authentication provider. This value is determined by the authentication provider.<br>At most one Group can have a given provider ID in a given Realm.<br>示例: `2838c8f3-d76a-4e99-acf1-1dee537e4c48` |

```json
{
  "providerId": "2838c8f3-d76a-4e99-acf1-1dee537e4c48"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `GetGroupProviderInfoPermissionDenied` | The provided token does not have permission to view the provider information for the given group. |
| INVALID_ARGUMENT | `CannotReplaceProviderInfoForPrincipalInProtectedRealm` | Provider information for Principals in this Realm cannot be replaced. |
| PERMISSION_DENIED | `ReplaceGroupProviderInfoPermissionDenied` | Could not replace the GroupProviderInfo. |
| NOT_FOUND | `GroupNotFound` | The given Group could not be found. |
| NOT_FOUND | `GroupProviderInfoNotFound` | The given GroupProviderInfo could not be found. |
