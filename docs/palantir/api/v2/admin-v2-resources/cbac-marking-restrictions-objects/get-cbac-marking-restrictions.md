`GET /api/v2/admin/cbacMarkingRestrictions`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Returns disallowed, implied, and required markings for the given set of marking IDs.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:admin-read`.

**OAuth2 scopes**: `api:admin-read`

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `markingIds` | list<MarkingId> | 否 | The marking IDs for which to get restrictions. |
| `markingIds.MarkingId` | string | 是 | The ID of a security marking. |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Response

**CbacMarkingRestrictions**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `CbacMarkingRestrictions` | object | 是 | 示例: `{"disallowedMarkings":["18212f9a-0e63-4b79-96a0-aae04df23336"],"requiredMarkings":[["18212f9a-0e63-4b79-96a0-aae04df23336"]],"userSatisfiesMarkings":true,"isValid":true,"impliedMarkings":["18212f9a-0e63-4b79-96a0-aae04df23336"]}` |
| `CbacMarkingRestrictions.disallowedMarkings` | list<MarkingId> | 否 | Markings that cannot appear in conjunction with the provided markings. This includes all such markings, not just those present in the provided set. |
| `CbacMarkingRestrictions.disallowedMarkings.MarkingId` | string | 是 | The ID of a security marking. |
| `CbacMarkingRestrictions.impliedMarkings` | list<MarkingId> | 否 | Markings that are automatically granted when a user has membership in any of the provided markings. |
| `CbacMarkingRestrictions.impliedMarkings.MarkingId` | string | 是 | The ID of a security marking. |
| `CbacMarkingRestrictions.requiredMarkings` | list<array> | 否 | Markings that must appear in conjunction with the provided markings. Each list contains the requirements for one of the provided markings, and at least one marking from each must be included in the provided markingIds to constitute a valid classification. |
| `CbacMarkingRestrictions.requiredMarkings.array` | list<MarkingId> | 是 | — |
| `CbacMarkingRestrictions.requiredMarkings.array.MarkingId` | string | 是 | The ID of a security marking. |
| `CbacMarkingRestrictions.userSatisfiesMarkings` | boolean | 是 | True if the current user satisfies the provided markings. The user must be a member of all conjunctive markings. The provided disjunctive markings are grouped by category, and the user must be a member of at least one marking in each group.<br>示例: `true` |
| `CbacMarkingRestrictions.isValid` | boolean | 是 | True if the provided markings constitute a valid classification, containing no disallowed markings and satisfying all required marking constraints.<br>示例: `true` |

```json
{
  "disallowedMarkings": [
    "18212f9a-0e63-4b79-96a0-aae04df23336"
  ],
  "requiredMarkings": [
    [
      "18212f9a-0e63-4b79-96a0-aae04df23336"
    ]
  ],
  "userSatisfiesMarkings": true,
  "isValid": true,
  "impliedMarkings": [
    "18212f9a-0e63-4b79-96a0-aae04df23336"
  ]
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `GetCbacMarkingRestrictionInfoPermissionDenied` | The provided token does not have permission to get the CBAC marking restrictions for the markings. |
| INVALID_ARGUMENT | `CbacUnavailable` | CBAC is not available. |
| NOT_FOUND | `CbacMarkingRestrictionsNotFound` | The given CbacMarkingRestrictions could not be found. |
