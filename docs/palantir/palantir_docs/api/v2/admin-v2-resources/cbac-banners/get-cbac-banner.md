`GET /api/v2/admin/cbacBanner`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Returns a classification banner string and colors for the given set of marking IDs.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:admin-read`.

**OAuth2 scopes**: `api:admin-read`

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `displayType` | enum | 否 | The display type of the banner. Defaults to PORTION_MARKING. BANNER_LINE is the long classification string used in the header of a document; PORTION_MARKING is a short classification string used for individual paragraphs<br>示例: `PORTION_MARKING` |
| `markingIds` | list<MarkingId> | 否 | The marking IDs for which to generate a banner. Duplicate entries are ignored.<br>示例: `["MTS","MNF"]` |
| `markingIds.MarkingId` | string | 是 | The ID of a security marking. |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Response

**CbacBanner**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `CbacBanner` | object | 是 | 示例: `{"markings":["MTS","MNF"],"backgroundColors":["#FFFFFF"],"classificationString":"MTS//MNF","textColor":"#FFFFFF"}` |
| `CbacBanner.classificationString` | string | 是 | The formatted classification string for the requested markings and display type.<br>示例: `MTS//MNF` |
| `CbacBanner.markings` | list<MarkingId> | 否 | The marking IDs represented by the classification string.<br>示例: `["MTS","MNF"]` |
| `CbacBanner.markings.MarkingId` | string | 是 | The ID of a security marking. |
| `CbacBanner.textColor` | string | 是 | The hexadecimal text color to use when displaying the banner.<br>示例: `#FFFFFF` |
| `CbacBanner.backgroundColors` | list<Color> | 否 | The hexadecimal background colors to use when displaying the banner. |
| `CbacBanner.backgroundColors.Color` | string | 是 | The hex value of a color. |

```json
{
  "markings": [
    "MTS",
    "MNF"
  ],
  "backgroundColors": [
    "#FFFFFF"
  ],
  "classificationString": "MTS//MNF",
  "textColor": "#FFFFFF"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `GetCbacBannerPermissionDenied` | The provided token does not have permission to get the CBAC banner for the markings. |
| INVALID_ARGUMENT | `CbacUnavailable` | CBAC is not available. |
| INVALID_ARGUMENT | `UnknownClassificationBannerDisplayType` | The provided classification banner display type is not recognized. |
| NOT_FOUND | `CbacBannerNotFound` | The given CbacBanner could not be found. |
