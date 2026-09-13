`POST /api/v2/admin/markingCategories`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Creates a new MarkingCategory.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:admin-write`.

**OAuth2 scopes**: `api:admin-write`

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Request body

```json
{
  "name": "Customer Data",
  "description": "Markings related to data about our customers",
  "initialPermissions": {
    "organizationRids": [
      "ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa"
    ],
    "roles": [
      {
        "role": "ADMINISTER",
        "principalId": "f05f8da4-b84c-4fca-9c77-8af0b13d11de"
      }
    ],
    "isPublic": false
  }
}
```

## Response

**MarkingCategory**

The created MarkingCategory

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `MarkingCategory` | object | 是 | The created MarkingCategory<br>示例: `{"categoryType":"CONJUNCTIVE","markings":["18212f9a-0e63-4b79-96a0-aae04df23336"],"createdBy":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","name":"Customer Data","description":"Markings related to data about our customers","createdTime":"2003-05-06T12:34:56.789Z","id":"0950264e-01c8-4e83-81a9-1a6b7f77621a","markingType":"MANDATORY"}` |
| `MarkingCategory.id` | string | 是 | The ID of a marking category. For user-created categories, this will be a UUID. Markings associated with<br>Organizations are placed in a category with ID "Organization".<br>示例: `0950264e-01c8-4e83-81a9-1a6b7f77621a` |
| `MarkingCategory.name` | string | 是 | 示例: `Customer Data` |
| `MarkingCategory.description` | string | 是 | 示例: `Markings related to data about our customers` |
| `MarkingCategory.categoryType` | enum | 是 | 示例: `CONJUNCTIVE` |
| `MarkingCategory.markingType` | enum | 是 | 示例: `MANDATORY` |
| `MarkingCategory.markings` | list<MarkingId> | 否 | — |
| `MarkingCategory.markings.MarkingId` | string | 是 | The ID of a security marking. |
| `MarkingCategory.createdTime` | string | 是 | The time at which the resource was created.<br>示例: `2003-05-06T12:34:56.789Z` |
| `MarkingCategory.createdBy` | string | 否 | The Foundry user who created this resource<br>示例: `f05f8da4-b84c-4fca-9c77-8af0b13d11de` |

```json
{
  "categoryType": "CONJUNCTIVE",
  "markings": [
    "18212f9a-0e63-4b79-96a0-aae04df23336"
  ],
  "createdBy": "f05f8da4-b84c-4fca-9c77-8af0b13d11de",
  "name": "Customer Data",
  "description": "Markings related to data about our customers",
  "createdTime": "2003-05-06T12:34:56.789Z",
  "id": "0950264e-01c8-4e83-81a9-1a6b7f77621a",
  "markingType": "MANDATORY"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| INVALID_ARGUMENT | `CreateMarkingCategoryMissingInitialAdminRole` | At least one ADMINISTER role assignment must be provided when creating a marking category. |
| INVALID_ARGUMENT | `CreateMarkingCategoryMissingOrganization` | At least one organization must be provided when creating a marking category. |
| NOT_FOUND | `PrincipalNotFound` | A principal (User or Group) with the given PrincipalId could not be found |
| PERMISSION_DENIED | `CreateMarkingCategoryPermissionDenied` | Could not create the MarkingCategory. |
