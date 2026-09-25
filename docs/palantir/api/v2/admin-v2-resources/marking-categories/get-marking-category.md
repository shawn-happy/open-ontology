`GET /api/v2/admin/markingCategories/{markingCategoryId}`

Get the MarkingCategory with the specified id.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:admin-read`.

**OAuth2 scopes**: `api:admin-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `markingCategoryId` | string | 是 | The ID of a marking category. For user-created categories, this will be a UUID. Markings associated with<br>Organizations are placed in a category with ID "Organization".<br>示例: `0950264e-01c8-4e83-81a9-1a6b7f77621a` |

## Response

**MarkingCategory**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `MarkingCategory` | object | 是 | 示例: `{"categoryType":"CONJUNCTIVE","markings":["18212f9a-0e63-4b79-96a0-aae04df23336"],"createdBy":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","name":"Customer Data","description":"Markings related to data about our customers","createdTime":"2003-05-06T12:34:56.789Z","id":"0950264e-01c8-4e83-81a9-1a6b7f77621a","markingType":"MANDATORY"}` |
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
| PERMISSION_DENIED | `GetMarkingCategoryPermissionDenied` | The provided token does not have permission to view the marking category. |
| NOT_FOUND | `MarkingCategoryNotFound` | The given MarkingCategory could not be found. |
