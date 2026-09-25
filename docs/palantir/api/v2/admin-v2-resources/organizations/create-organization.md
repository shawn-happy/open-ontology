`POST /api/v2/admin/organizations`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Creates a new Organization.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:admin-write`.

**OAuth2 scopes**: `api:admin-write`

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Request body

```json
{
  "enrollmentRid": "ri.control-panel.main.customer.466f812b-f974-4478-9d4f-90402cd3def6",
  "name": "Example Organization",
  "host": "example.palantirfoundry.com",
  "administrators": [
    "f05f8da4-b84c-4fca-9c77-8af0b13d11de"
  ]
}
```

## Response

**Organization**

The created Organization

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `Organization` | object | 是 | The created Organization<br>示例: `{"name":"Example Organization","host":"example.palantirfoundry.com","rid":"ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa","markingId":"18212f9a-0e63-4b79-96a0-aae04df23336"}` |
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
| INVALID_ARGUMENT | `CreateOrganizationMissingInitialAdminRole` | At least one organization:administrator role grant must be provided when creating a organization. |
| INVALID_ARGUMENT | `OrganizationNameAlreadyExists` | An organization with the same name already exists. |
| NOT_FOUND | `PrincipalNotFound` | A principal (User or Group) with the given PrincipalId could not be found |
| PERMISSION_DENIED | `CreateOrganizationPermissionDenied` | Could not create the Organization. |
| NOT_FOUND | `EnrollmentNotFound` | The given Enrollment could not be found. |
| NOT_FOUND | `OrganizationNotFound` | The given Organization could not be found. |
