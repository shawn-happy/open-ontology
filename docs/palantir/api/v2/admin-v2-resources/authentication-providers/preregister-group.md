`POST /api/v2/admin/enrollments/{enrollmentRid}/authenticationProviders/{authenticationProviderRid}/preregisterGroup`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Register a Group with a given name before any users with this group log in through this Authentication Provider.
Preregistered groups can be used anywhere other groups are used in the platform.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:admin-write`.

**OAuth2 scopes**: `api:admin-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `enrollmentRid` | string | 是 | 示例: `ri.control-panel.main.customer.466f812b-f974-4478-9d4f-90402cd3def6` |
| `authenticationProviderRid` | string | 是 | 示例: `ri.control-panel.main.saml.3faf689c-eaa1-4137-851f-81d58afe4c86` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Request body

```json
{
  "name": "Data Source Admins",
  "organizations": [
    "ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa"
  ]
}
```

## Response

**PrincipalId**

The ID of a Foundry Group or User.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `PrincipalId` | string | 是 | The ID of a Foundry Group or User.<br>示例: `f05f8da4-b84c-4fca-9c77-8af0b13d11de` |

```text
f05f8da4-b84c-4fca-9c77-8af0b13d11de
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `PreregisterGroupPermissionDenied` | Could not preregisterGroup the AuthenticationProvider. |
| NOT_FOUND | `AuthenticationProviderNotFound` | The given AuthenticationProvider could not be found. |
