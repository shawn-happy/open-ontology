`PUT /api/v2/dataHealth/checks/{checkRid}`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Replace the Check with the specified rid. Changing the type of a check after it has been created is not supported.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:data-health-write`.

**OAuth2 scopes**: `api:data-health-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `checkRid` | string | 是 | The unique resource identifier (RID) of a Data Health Check.<br>示例: `ri.data-health.main.check.8e27b13a-e21b-4232-ae1b-76ccf5ff42b3` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Request body

```json
{
  "intent": "Check to ensure builds are passing."
}
```

## Response

**Check**

The replaced Check

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `Check` | object | 是 | The replaced Check<br>示例: `{"updatedTime":"2024-09-25T17:29:35.974Z","createdBy":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","groups":["ri.data-health.main.check-group.08e376a8-607d-4f44-b8dd-b4587be6ce9b"],"rid":"ri.data-health.main.check.8e27b13a-e21b-4232-ae1b-76ccf5ff42b3","intent":"Check to ensure builds are passing."}` |
| `Check.rid` | string | 是 | The unique resource identifier (RID) of a Data Health Check.<br>示例: `ri.data-health.main.check.8e27b13a-e21b-4232-ae1b-76ccf5ff42b3` |
| `Check.groups` | list<CheckGroupRid> | 否 | — |
| `Check.groups.CheckGroupRid` | string | 是 | The unique resource identifier (RID) of a CheckGroup. |
| `Check.config` | union | 是 | Configuration of a check. |
| `Check.config.numericColumnRange` | object | 否 | Checks that values in a numeric column fall within a specified range. |
| `Check.config.numericColumnRange.subject` | object | 是 | A dataset resource type.<br>示例: `{"datasetRid":"ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da","branchId":"master"}` |
| `Check.config.numericColumnRange.subject.datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `Check.config.numericColumnRange.subject.branchId` | string | 是 | The name of a Branch.<br>示例: `master` |
| `Check.config.numericColumnRange.columnName` | string | 是 | 示例: `column` |
| `Check.config.numericColumnRange.numericBoundsConfig` | object | 是 | Configuration for numeric bounds check with severity settings.<br>示例: `{"severity":"MODERATE","numericBounds":{"upperBound":1000.0,"lowerBound":0.0}}` |
| `Check.config.numericColumnRange.numericBoundsConfig.numericBounds` | object | 是 | The range of numeric values a check is expected to be within.<br>示例: `{"upperBound":1000.0,"lowerBound":0.0}` |
| `Check.config.numericColumnRange.numericBoundsConfig.numericBounds.lowerBound` | number | 否 | 示例: `0.0` |
| `Check.config.numericColumnRange.numericBoundsConfig.numericBounds.upperBound` | number | 否 | 示例: `1000.0` |
| `Check.config.numericColumnRange.numericBoundsConfig.severity` | enum | 是 | The severity level of the check. Possible values are MODERATE or CRITICAL.<br>示例: `MODERATE` |
| `Check.config.jobStatus` | object | 否 | Checks the status of the most recent job run on the dataset. |
| `Check.config.jobStatus.subject` | object | 是 | A dataset resource type.<br>示例: `{"datasetRid":"ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da","branchId":"master"}` |
| `Check.config.jobStatus.subject.datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `Check.config.jobStatus.subject.branchId` | string | 是 | The name of a Branch.<br>示例: `master` |
| `Check.config.jobStatus.statusCheckConfig` | object | 是 | 示例: `{"severity":"MODERATE","escalationConfig":{"timeIntervalInSeconds":30,"failuresToCritical":1}}` |
| `Check.config.jobStatus.statusCheckConfig.severity` | enum | 是 | The severity level of the check. Possible values are MODERATE or CRITICAL.<br>示例: `MODERATE` |
| `Check.config.jobStatus.statusCheckConfig.escalationConfig` | object | 否 | The configuration for when the severity of the failing health check should be escalated to CRITICAL â after a given number of failures, possibly within a time interval.<br>示例: `{"timeIntervalInSeconds":30,"failuresToCritical":1}` |
| `Check.config.jobStatus.statusCheckConfig.escalationConfig.failuresToCritical` | integer | 是 | 示例: `1` |
| `Check.config.jobStatus.statusCheckConfig.escalationConfig.timeIntervalInSeconds` | string | 否 | 示例: `30` |
| `Check.config.numericColumnMean` | object | 否 | Checks the mean value of a numeric column. |
| `Check.config.numericColumnMean.subject` | object | 是 | A dataset resource type.<br>示例: `{"datasetRid":"ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da","branchId":"master"}` |
| `Check.config.numericColumnMean.subject.datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `Check.config.numericColumnMean.subject.branchId` | string | 是 | The name of a Branch.<br>示例: `master` |
| `Check.config.numericColumnMean.numericColumnCheckConfig` | object | 是 | Configuration for numeric column-based checks (such as mean or median). At least one of numericBounds or trend must be specified. Both may be provided to validate both the absolute value range and the trend behavior over time.<br>示例: `{"numericBounds":{"severity":"MODERATE","numericBounds":{"upperBound":1000.0,"lowerBound":0.0}},"trend":{"severity":"MODERATE","differenceBounds":{"upperBound":1000.0,"lowerBound":0.0},"trendType":"NON_DECREASING"},"columnName":"amount"}` |
| `Check.config.numericColumnMean.numericColumnCheckConfig.columnName` | string | 是 | 示例: `amount` |
| `Check.config.numericColumnMean.numericColumnCheckConfig.numericBounds` | object | 否 | Configuration for numeric bounds check with severity settings.<br>示例: `{"severity":"MODERATE","numericBounds":{"upperBound":1000.0,"lowerBound":0.0}}` |
| `Check.config.numericColumnMean.numericColumnCheckConfig.numericBounds.numericBounds` | object | 是 | The range of numeric values a check is expected to be within.<br>示例: `{"upperBound":1000.0,"lowerBound":0.0}` |
| `Check.config.numericColumnMean.numericColumnCheckConfig.numericBounds.numericBounds.lowerBound` | number | 否 | 示例: `0.0` |
| `Check.config.numericColumnMean.numericColumnCheckConfig.numericBounds.numericBounds.upperBound` | number | 否 | 示例: `1000.0` |
| `Check.config.numericColumnMean.numericColumnCheckConfig.numericBounds.severity` | enum | 是 | The severity level of the check. Possible values are MODERATE or CRITICAL.<br>示例: `MODERATE` |
| `Check.config.numericColumnMean.numericColumnCheckConfig.trend` | object | 否 | Configuration for trend-based validation with severity settings. At least one of trendType or differenceBounds must be specified. Both may be provided to validate both the trend pattern and the magnitude of change.<br>示例: `{"severity":"MODERATE","differenceBounds":{"upperBound":1000.0,"lowerBound":0.0},"trendType":"NON_DECREASING"}` |
| `Check.config.numericColumnMean.numericColumnCheckConfig.trend.trendType` | enum | 否 | The type of trend to validate:<br>- NON_INCREASING: Values should not increase over time<br>- NON_DECREASING: Values should not decrease over time<br>- STRICTLY_INCREASING: Values should strictly increase over time<br>- STRICTLY_DECREASING: Values should strictly decrease over time<br>- CONSTANT: Values should remain constant over time<br>示例: `NON_DECREASING` |
| `Check.config.numericColumnMean.numericColumnCheckConfig.trend.differenceBounds` | object | 否 | The range of numeric values a check is expected to be within.<br>示例: `{"upperBound":1000.0,"lowerBound":0.0}` |
| `Check.config.numericColumnMean.numericColumnCheckConfig.trend.differenceBounds.lowerBound` | number | 否 | 示例: `0.0` |
| `Check.config.numericColumnMean.numericColumnCheckConfig.trend.differenceBounds.upperBound` | number | 否 | 示例: `1000.0` |
| `Check.config.numericColumnMean.numericColumnCheckConfig.trend.severity` | enum | 是 | The severity level of the check. Possible values are MODERATE or CRITICAL.<br>示例: `MODERATE` |
| `Check.config.dateColumnRange` | object | 否 | Checks that values in a date column fall within a specified range. |
| `Check.config.dateColumnRange.subject` | object | 是 | A dataset resource type.<br>示例: `{"datasetRid":"ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da","branchId":"master"}` |
| `Check.config.dateColumnRange.subject.datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `Check.config.dateColumnRange.subject.branchId` | string | 是 | The name of a Branch.<br>示例: `master` |
| `Check.config.dateColumnRange.columnName` | string | 是 | 示例: `column` |
| `Check.config.dateColumnRange.dateBoundsConfig` | object | 是 | Configuration for date bounds check with severity settings.<br>示例: `{"severity":"MODERATE","dateBounds":{"upperBound":"2024-12-31T23:59:59Z","lowerBound":"2024-01-01T00:00:00Z"}}` |
| `Check.config.dateColumnRange.dateBoundsConfig.dateBounds` | object | 是 | The range of date values a check is expected to be within.<br>示例: `{"upperBound":"2024-12-31T23:59:59Z","lowerBound":"2024-01-01T00:00:00Z"}` |
| `Check.config.dateColumnRange.dateBoundsConfig.dateBounds.lowerBound` | string | 否 | 示例: `2024-01-01T00:00:00Z` |
| `Check.config.dateColumnRange.dateBoundsConfig.dateBounds.upperBound` | string | 否 | 示例: `2024-12-31T23:59:59Z` |
| `Check.config.dateColumnRange.dateBoundsConfig.severity` | enum | 是 | The severity level of the check. Possible values are MODERATE or CRITICAL.<br>示例: `MODERATE` |
| `Check.config.jobDuration` | object | 否 | Checks the total time a job takes to complete. |
| `Check.config.jobDuration.subject` | object | 是 | A dataset resource type.<br>示例: `{"datasetRid":"ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da","branchId":"master"}` |
| `Check.config.jobDuration.subject.datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `Check.config.jobDuration.subject.branchId` | string | 是 | The name of a Branch.<br>示例: `master` |
| `Check.config.jobDuration.timeCheckConfig` | object | 是 | 示例: `{"timeBounds":{"severity":"MODERATE","timeBounds":{"lowerBoundInSeconds":3600,"upperBoundInSeconds":3600}},"medianDeviation":{"severity":"MODERATE","medianDeviation":{"deviationThreshold":2.0,"dataPoints":10,"boundsType":"TWO_TAILED"}}}` |
| `Check.config.jobDuration.timeCheckConfig.timeBounds` | object | 否 | Configuration for time bounds check with severity settings.<br>示例: `{"severity":"MODERATE","timeBounds":{"lowerBoundInSeconds":3600,"upperBoundInSeconds":3600}}` |
| `Check.config.jobDuration.timeCheckConfig.timeBounds.timeBounds` | object | 是 | The configuration for the range of time between which the health check is expected to succeed.<br>示例: `{"lowerBoundInSeconds":3600,"upperBoundInSeconds":3600}` |
| `Check.config.jobDuration.timeCheckConfig.timeBounds.timeBounds.lowerBoundInSeconds` | string | 否 | 示例: `3600` |
| `Check.config.jobDuration.timeCheckConfig.timeBounds.timeBounds.upperBoundInSeconds` | string | 否 | 示例: `3600` |
| `Check.config.jobDuration.timeCheckConfig.timeBounds.severity` | enum | 是 | The severity level of the check. Possible values are MODERATE or CRITICAL.<br>示例: `MODERATE` |
| `Check.config.jobDuration.timeCheckConfig.medianDeviation` | object | 否 | Configuration for median deviation check with severity settings.<br>示例: `{"severity":"MODERATE","medianDeviation":{"deviationThreshold":2.0,"dataPoints":10,"boundsType":"TWO_TAILED"}}` |
| `Check.config.jobDuration.timeCheckConfig.medianDeviation.medianDeviation` | object | 是 | The number of thresholds the build's duration differs from the median.<br>示例: `{"deviationThreshold":2.0,"dataPoints":10,"boundsType":"TWO_TAILED"}` |
| `Check.config.jobDuration.timeCheckConfig.medianDeviation.medianDeviation.boundsType` | enum | 否 | The three types of median deviations a bounds type can have: - LOWER_BOUND â Tests for significant deviations below the median value, - UPPER_BOUND â Tests for significant deviations above the median value, - TWO_TAILED â Tests for significant deviations in either direction from the median value.<br>示例: `TWO_TAILED` |
| `Check.config.jobDuration.timeCheckConfig.medianDeviation.medianDeviation.dataPoints` | integer | 是 | 示例: `10` |
| `Check.config.jobDuration.timeCheckConfig.medianDeviation.medianDeviation.deviationThreshold` | number | 是 | 示例: `2.0` |
| `Check.config.jobDuration.timeCheckConfig.medianDeviation.severity` | enum | 是 | The severity level of the check. Possible values are MODERATE or CRITICAL.<br>示例: `MODERATE` |
| `Check.config.approximateUniquePercentage` | object | 否 | Checks the approximate percentage of unique values in a specific column. |
| `Check.config.approximateUniquePercentage.subject` | object | 是 | A dataset resource type.<br>示例: `{"datasetRid":"ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da","branchId":"master"}` |
| `Check.config.approximateUniquePercentage.subject.datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `Check.config.approximateUniquePercentage.subject.branchId` | string | 是 | The name of a Branch.<br>示例: `master` |
| `Check.config.approximateUniquePercentage.percentageCheckConfig` | object | 是 | Configuration for percentage-based checks (such as null percentage).<br>示例: `{"medianDeviation":{"severity":"MODERATE","medianDeviation":{"deviationThreshold":2.0,"dataPoints":10,"boundsType":"TWO_TAILED"}},"percentageBounds":{"severity":"MODERATE","percentageBounds":{"upperBoundPercentage":50.0,"lowerBoundPercentage":50.0}},"columnName":"user_id"}` |
| `Check.config.approximateUniquePercentage.percentageCheckConfig.columnName` | string | 是 | 示例: `user_id` |
| `Check.config.approximateUniquePercentage.percentageCheckConfig.percentageBounds` | object | 否 | Configuration for percentage bounds check with severity settings.<br>示例: `{"severity":"MODERATE","percentageBounds":{"upperBoundPercentage":50.0,"lowerBoundPercentage":50.0}}` |
| `Check.config.approximateUniquePercentage.percentageCheckConfig.percentageBounds.percentageBounds` | object | 是 | The configuration for the range of percentage values between which the health check is expected to succeed.<br>示例: `{"upperBoundPercentage":50.0,"lowerBoundPercentage":50.0}` |
| `Check.config.approximateUniquePercentage.percentageCheckConfig.percentageBounds.percentageBounds.lowerBoundPercentage` | number | 否 | A percentage value in the range 0.0 to 100.0.<br>Validation rules:<br>* must be greater than or equal to 0.0<br>* must be less than or equal to 100.0<br>示例: `50.0` |
| `Check.config.approximateUniquePercentage.percentageCheckConfig.percentageBounds.percentageBounds.upperBoundPercentage` | number | 否 | A percentage value in the range 0.0 to 100.0.<br>Validation rules:<br>* must be greater than or equal to 0.0<br>* must be less than or equal to 100.0<br>示例: `50.0` |
| `Check.config.approximateUniquePercentage.percentageCheckConfig.percentageBounds.severity` | enum | 是 | The severity level of the check. Possible values are MODERATE or CRITICAL.<br>示例: `MODERATE` |
| `Check.config.approximateUniquePercentage.percentageCheckConfig.medianDeviation` | object | 否 | Configuration for median deviation check with severity settings.<br>示例: `{"severity":"MODERATE","medianDeviation":{"deviationThreshold":2.0,"dataPoints":10,"boundsType":"TWO_TAILED"}}` |
| `Check.config.approximateUniquePercentage.percentageCheckConfig.medianDeviation.medianDeviation` | object | 是 | The number of thresholds the build's duration differs from the median.<br>示例: `{"deviationThreshold":2.0,"dataPoints":10,"boundsType":"TWO_TAILED"}` |
| `Check.config.approximateUniquePercentage.percentageCheckConfig.medianDeviation.medianDeviation.boundsType` | enum | 否 | The three types of median deviations a bounds type can have: - LOWER_BOUND â Tests for significant deviations below the median value, - UPPER_BOUND â Tests for significant deviations above the median value, - TWO_TAILED â Tests for significant deviations in either direction from the median value.<br>示例: `TWO_TAILED` |
| `Check.config.approximateUniquePercentage.percentageCheckConfig.medianDeviation.medianDeviation.dataPoints` | integer | 是 | 示例: `10` |
| `Check.config.approximateUniquePercentage.percentageCheckConfig.medianDeviation.medianDeviation.deviationThreshold` | number | 是 | 示例: `2.0` |
| `Check.config.approximateUniquePercentage.percentageCheckConfig.medianDeviation.severity` | enum | 是 | The severity level of the check. Possible values are MODERATE or CRITICAL.<br>示例: `MODERATE` |
| `Check.config.buildStatus` | object | 否 | Checks the status of the most recent build of the dataset. |
| `Check.config.buildStatus.subject` | object | 是 | A dataset resource type.<br>示例: `{"datasetRid":"ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da","branchId":"master"}` |
| `Check.config.buildStatus.subject.datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `Check.config.buildStatus.subject.branchId` | string | 是 | The name of a Branch.<br>示例: `master` |
| `Check.config.buildStatus.statusCheckConfig` | object | 是 | 示例: `{"severity":"MODERATE","escalationConfig":{"timeIntervalInSeconds":30,"failuresToCritical":1}}` |
| `Check.config.buildStatus.statusCheckConfig.severity` | enum | 是 | The severity level of the check. Possible values are MODERATE or CRITICAL.<br>示例: `MODERATE` |
| `Check.config.buildStatus.statusCheckConfig.escalationConfig` | object | 否 | The configuration for when the severity of the failing health check should be escalated to CRITICAL â after a given number of failures, possibly within a time interval.<br>示例: `{"timeIntervalInSeconds":30,"failuresToCritical":1}` |
| `Check.config.buildStatus.statusCheckConfig.escalationConfig.failuresToCritical` | integer | 是 | 示例: `1` |
| `Check.config.buildStatus.statusCheckConfig.escalationConfig.timeIntervalInSeconds` | string | 否 | 示例: `30` |
| `Check.config.columnType` | object | 否 | Checks the existence and optionally the type of a specific column. |
| `Check.config.columnType.subject` | object | 是 | A dataset resource type.<br>示例: `{"datasetRid":"ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da","branchId":"master"}` |
| `Check.config.columnType.subject.datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `Check.config.columnType.subject.branchId` | string | 是 | The name of a Branch.<br>示例: `master` |
| `Check.config.columnType.columnTypeConfig` | object | 是 | Configuration for column type validation with severity settings.<br>示例: `{"severity":"MODERATE","expectedType":"STRING","columnName":"user_id"}` |
| `Check.config.columnType.columnTypeConfig.columnName` | string | 是 | 示例: `user_id` |
| `Check.config.columnType.columnTypeConfig.expectedType` | enum | 否 | The data type of a column in a dataset schema.<br>示例: `STRING` |
| `Check.config.columnType.columnTypeConfig.severity` | enum | 是 | The severity level of the check. Possible values are MODERATE or CRITICAL.<br>示例: `MODERATE` |
| `Check.config.allowedColumnValues` | object | 否 | Checks that values in a column are within an allowed set of values. |
| `Check.config.allowedColumnValues.subject` | object | 是 | A dataset resource type.<br>示例: `{"datasetRid":"ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da","branchId":"master"}` |
| `Check.config.allowedColumnValues.subject.datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `Check.config.allowedColumnValues.subject.branchId` | string | 是 | The name of a Branch.<br>示例: `master` |
| `Check.config.allowedColumnValues.columnName` | string | 是 | 示例: `status` |
| `Check.config.allowedColumnValues.allowedValues` | list<ColumnValue> | 否 | — |
| `Check.config.allowedColumnValues.allowedValues.ColumnValue` | union | 是 | A column value that can be of different types. |
| `Check.config.allowedColumnValues.allowedValues.ColumnValue.date` | object | 否 | A date column value. |
| `Check.config.allowedColumnValues.allowedValues.ColumnValue.date.value` | string | 是 | 示例: `2024-01-01T00:00:00Z` |
| `Check.config.allowedColumnValues.allowedValues.ColumnValue.boolean` | object | 否 | A boolean column value. |
| `Check.config.allowedColumnValues.allowedValues.ColumnValue.boolean.value` | boolean | 是 | 示例: `true` |
| `Check.config.allowedColumnValues.allowedValues.ColumnValue.string` | object | 否 | A string column value. |
| `Check.config.allowedColumnValues.allowedValues.ColumnValue.string.value` | string | 是 | 示例: `approved` |
| `Check.config.allowedColumnValues.allowedValues.ColumnValue.numeric` | object | 否 | A numeric column value. |
| `Check.config.allowedColumnValues.allowedValues.ColumnValue.numeric.value` | number | 是 | 示例: `42.0` |
| `Check.config.allowedColumnValues.allowNull` | boolean | 否 | 示例: `true` |
| `Check.config.allowedColumnValues.severity` | enum | 是 | The severity level of the check. Possible values are MODERATE or CRITICAL.<br>示例: `MODERATE` |
| `Check.config.timeSinceLastUpdated` | object | 否 | Checks the total time since the dataset has updated. |
| `Check.config.timeSinceLastUpdated.subject` | object | 是 | A dataset resource type.<br>示例: `{"datasetRid":"ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da","branchId":"master"}` |
| `Check.config.timeSinceLastUpdated.subject.datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `Check.config.timeSinceLastUpdated.subject.branchId` | string | 是 | The name of a Branch.<br>示例: `master` |
| `Check.config.timeSinceLastUpdated.timeCheckConfig` | object | 是 | Defines the configuration of a transaction-based time check.<br>示例: `{"timeBounds":{"severity":"MODERATE","timeBounds":{"lowerBoundInSeconds":3600,"upperBoundInSeconds":3600}},"medianDeviation":{"severity":"MODERATE","medianDeviation":{"deviationThreshold":2.0,"dataPoints":10,"boundsType":"TWO_TAILED"}}}` |
| `Check.config.timeSinceLastUpdated.timeCheckConfig.timeBounds` | object | 否 | Configuration for time bounds check with severity settings.<br>示例: `{"severity":"MODERATE","timeBounds":{"lowerBoundInSeconds":3600,"upperBoundInSeconds":3600}}` |
| `Check.config.timeSinceLastUpdated.timeCheckConfig.timeBounds.timeBounds` | object | 是 | The configuration for the range of time between which the health check is expected to succeed.<br>示例: `{"lowerBoundInSeconds":3600,"upperBoundInSeconds":3600}` |
| `Check.config.timeSinceLastUpdated.timeCheckConfig.timeBounds.timeBounds.lowerBoundInSeconds` | string | 否 | 示例: `3600` |
| `Check.config.timeSinceLastUpdated.timeCheckConfig.timeBounds.timeBounds.upperBoundInSeconds` | string | 否 | 示例: `3600` |
| `Check.config.timeSinceLastUpdated.timeCheckConfig.timeBounds.severity` | enum | 是 | The severity level of the check. Possible values are MODERATE or CRITICAL.<br>示例: `MODERATE` |
| `Check.config.timeSinceLastUpdated.timeCheckConfig.medianDeviation` | object | 否 | Configuration for median deviation check with severity settings.<br>示例: `{"severity":"MODERATE","medianDeviation":{"deviationThreshold":2.0,"dataPoints":10,"boundsType":"TWO_TAILED"}}` |
| `Check.config.timeSinceLastUpdated.timeCheckConfig.medianDeviation.medianDeviation` | object | 是 | The number of thresholds the build's duration differs from the median.<br>示例: `{"deviationThreshold":2.0,"dataPoints":10,"boundsType":"TWO_TAILED"}` |
| `Check.config.timeSinceLastUpdated.timeCheckConfig.medianDeviation.medianDeviation.boundsType` | enum | 否 | The three types of median deviations a bounds type can have: - LOWER_BOUND â Tests for significant deviations below the median value, - UPPER_BOUND â Tests for significant deviations above the median value, - TWO_TAILED â Tests for significant deviations in either direction from the median value.<br>示例: `TWO_TAILED` |
| `Check.config.timeSinceLastUpdated.timeCheckConfig.medianDeviation.medianDeviation.dataPoints` | integer | 是 | 示例: `10` |
| `Check.config.timeSinceLastUpdated.timeCheckConfig.medianDeviation.medianDeviation.deviationThreshold` | number | 是 | 示例: `2.0` |
| `Check.config.timeSinceLastUpdated.timeCheckConfig.medianDeviation.severity` | enum | 是 | The severity level of the check. Possible values are MODERATE or CRITICAL.<br>示例: `MODERATE` |
| `Check.config.timeSinceLastUpdated.timeCheckConfig.ignoreEmptyTransactions` | boolean | 否 | Whether empty transactions should be ignored when calculating time since last updated. If true (default), only transactions with actual data changes are considered. |
| `Check.config.scheduleStatus` | object | 否 | Checks the status of the most recent schedule run. |
| `Check.config.scheduleStatus.subject` | object | 是 | A schedule resource type.<br>示例: `{"scheduleRid":"ri.scheduler.main.schedule.8843955e-37d1-4363-85eb-539833e10a41"}` |
| `Check.config.scheduleStatus.subject.scheduleRid` | string | 是 | The RID of a Schedule.<br>示例: `ri.scheduler.main.schedule.8843955e-37d1-4363-85eb-539833e10a41` |
| `Check.config.scheduleStatus.statusCheckConfig` | object | 是 | 示例: `{"severity":"MODERATE","escalationConfig":{"timeIntervalInSeconds":30,"failuresToCritical":1}}` |
| `Check.config.scheduleStatus.statusCheckConfig.severity` | enum | 是 | The severity level of the check. Possible values are MODERATE or CRITICAL.<br>示例: `MODERATE` |
| `Check.config.scheduleStatus.statusCheckConfig.escalationConfig` | object | 否 | The configuration for when the severity of the failing health check should be escalated to CRITICAL â after a given number of failures, possibly within a time interval.<br>示例: `{"timeIntervalInSeconds":30,"failuresToCritical":1}` |
| `Check.config.scheduleStatus.statusCheckConfig.escalationConfig.failuresToCritical` | integer | 是 | 示例: `1` |
| `Check.config.scheduleStatus.statusCheckConfig.escalationConfig.timeIntervalInSeconds` | string | 否 | 示例: `30` |
| `Check.config.nullPercentage` | object | 否 | Checks the percentage of null values in a specific column. |
| `Check.config.nullPercentage.subject` | object | 是 | A dataset resource type.<br>示例: `{"datasetRid":"ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da","branchId":"master"}` |
| `Check.config.nullPercentage.subject.datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `Check.config.nullPercentage.subject.branchId` | string | 是 | The name of a Branch.<br>示例: `master` |
| `Check.config.nullPercentage.percentageCheckConfig` | object | 是 | Configuration for percentage-based checks (such as null percentage).<br>示例: `{"medianDeviation":{"severity":"MODERATE","medianDeviation":{"deviationThreshold":2.0,"dataPoints":10,"boundsType":"TWO_TAILED"}},"percentageBounds":{"severity":"MODERATE","percentageBounds":{"upperBoundPercentage":50.0,"lowerBoundPercentage":50.0}},"columnName":"user_id"}` |
| `Check.config.nullPercentage.percentageCheckConfig.columnName` | string | 是 | 示例: `user_id` |
| `Check.config.nullPercentage.percentageCheckConfig.percentageBounds` | object | 否 | Configuration for percentage bounds check with severity settings.<br>示例: `{"severity":"MODERATE","percentageBounds":{"upperBoundPercentage":50.0,"lowerBoundPercentage":50.0}}` |
| `Check.config.nullPercentage.percentageCheckConfig.percentageBounds.percentageBounds` | object | 是 | The configuration for the range of percentage values between which the health check is expected to succeed.<br>示例: `{"upperBoundPercentage":50.0,"lowerBoundPercentage":50.0}` |
| `Check.config.nullPercentage.percentageCheckConfig.percentageBounds.percentageBounds.lowerBoundPercentage` | number | 否 | A percentage value in the range 0.0 to 100.0.<br>Validation rules:<br>* must be greater than or equal to 0.0<br>* must be less than or equal to 100.0<br>示例: `50.0` |
| `Check.config.nullPercentage.percentageCheckConfig.percentageBounds.percentageBounds.upperBoundPercentage` | number | 否 | A percentage value in the range 0.0 to 100.0.<br>Validation rules:<br>* must be greater than or equal to 0.0<br>* must be less than or equal to 100.0<br>示例: `50.0` |
| `Check.config.nullPercentage.percentageCheckConfig.percentageBounds.severity` | enum | 是 | The severity level of the check. Possible values are MODERATE or CRITICAL.<br>示例: `MODERATE` |
| `Check.config.nullPercentage.percentageCheckConfig.medianDeviation` | object | 否 | Configuration for median deviation check with severity settings.<br>示例: `{"severity":"MODERATE","medianDeviation":{"deviationThreshold":2.0,"dataPoints":10,"boundsType":"TWO_TAILED"}}` |
| `Check.config.nullPercentage.percentageCheckConfig.medianDeviation.medianDeviation` | object | 是 | The number of thresholds the build's duration differs from the median.<br>示例: `{"deviationThreshold":2.0,"dataPoints":10,"boundsType":"TWO_TAILED"}` |
| `Check.config.nullPercentage.percentageCheckConfig.medianDeviation.medianDeviation.boundsType` | enum | 否 | The three types of median deviations a bounds type can have: - LOWER_BOUND â Tests for significant deviations below the median value, - UPPER_BOUND â Tests for significant deviations above the median value, - TWO_TAILED â Tests for significant deviations in either direction from the median value.<br>示例: `TWO_TAILED` |
| `Check.config.nullPercentage.percentageCheckConfig.medianDeviation.medianDeviation.dataPoints` | integer | 是 | 示例: `10` |
| `Check.config.nullPercentage.percentageCheckConfig.medianDeviation.medianDeviation.deviationThreshold` | number | 是 | 示例: `2.0` |
| `Check.config.nullPercentage.percentageCheckConfig.medianDeviation.severity` | enum | 是 | The severity level of the check. Possible values are MODERATE or CRITICAL.<br>示例: `MODERATE` |
| `Check.config.scheduleDuration` | object | 否 | Checks the total time a schedule takes to complete. |
| `Check.config.scheduleDuration.subject` | object | 是 | A schedule resource type.<br>示例: `{"scheduleRid":"ri.scheduler.main.schedule.8843955e-37d1-4363-85eb-539833e10a41"}` |
| `Check.config.scheduleDuration.subject.scheduleRid` | string | 是 | The RID of a Schedule.<br>示例: `ri.scheduler.main.schedule.8843955e-37d1-4363-85eb-539833e10a41` |
| `Check.config.scheduleDuration.timeCheckConfig` | object | 是 | 示例: `{"timeBounds":{"severity":"MODERATE","timeBounds":{"lowerBoundInSeconds":3600,"upperBoundInSeconds":3600}},"medianDeviation":{"severity":"MODERATE","medianDeviation":{"deviationThreshold":2.0,"dataPoints":10,"boundsType":"TWO_TAILED"}}}` |
| `Check.config.scheduleDuration.timeCheckConfig.timeBounds` | object | 否 | Configuration for time bounds check with severity settings.<br>示例: `{"severity":"MODERATE","timeBounds":{"lowerBoundInSeconds":3600,"upperBoundInSeconds":3600}}` |
| `Check.config.scheduleDuration.timeCheckConfig.timeBounds.timeBounds` | object | 是 | The configuration for the range of time between which the health check is expected to succeed.<br>示例: `{"lowerBoundInSeconds":3600,"upperBoundInSeconds":3600}` |
| `Check.config.scheduleDuration.timeCheckConfig.timeBounds.timeBounds.lowerBoundInSeconds` | string | 否 | 示例: `3600` |
| `Check.config.scheduleDuration.timeCheckConfig.timeBounds.timeBounds.upperBoundInSeconds` | string | 否 | 示例: `3600` |
| `Check.config.scheduleDuration.timeCheckConfig.timeBounds.severity` | enum | 是 | The severity level of the check. Possible values are MODERATE or CRITICAL.<br>示例: `MODERATE` |
| `Check.config.scheduleDuration.timeCheckConfig.medianDeviation` | object | 否 | Configuration for median deviation check with severity settings.<br>示例: `{"severity":"MODERATE","medianDeviation":{"deviationThreshold":2.0,"dataPoints":10,"boundsType":"TWO_TAILED"}}` |
| `Check.config.scheduleDuration.timeCheckConfig.medianDeviation.medianDeviation` | object | 是 | The number of thresholds the build's duration differs from the median.<br>示例: `{"deviationThreshold":2.0,"dataPoints":10,"boundsType":"TWO_TAILED"}` |
| `Check.config.scheduleDuration.timeCheckConfig.medianDeviation.medianDeviation.boundsType` | enum | 否 | The three types of median deviations a bounds type can have: - LOWER_BOUND â Tests for significant deviations below the median value, - UPPER_BOUND â Tests for significant deviations above the median value, - TWO_TAILED â Tests for significant deviations in either direction from the median value.<br>示例: `TWO_TAILED` |
| `Check.config.scheduleDuration.timeCheckConfig.medianDeviation.medianDeviation.dataPoints` | integer | 是 | 示例: `10` |
| `Check.config.scheduleDuration.timeCheckConfig.medianDeviation.medianDeviation.deviationThreshold` | number | 是 | 示例: `2.0` |
| `Check.config.scheduleDuration.timeCheckConfig.medianDeviation.severity` | enum | 是 | The severity level of the check. Possible values are MODERATE or CRITICAL.<br>示例: `MODERATE` |
| `Check.config.totalColumnCount` | object | 否 | Checks the total number of columns in the dataset. |
| `Check.config.totalColumnCount.subject` | object | 是 | A dataset resource type.<br>示例: `{"datasetRid":"ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da","branchId":"master"}` |
| `Check.config.totalColumnCount.subject.datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `Check.config.totalColumnCount.subject.branchId` | string | 是 | The name of a Branch.<br>示例: `master` |
| `Check.config.totalColumnCount.columnCountConfig` | object | 是 | Configuration for column count validation with severity settings.<br>示例: `{"severity":"MODERATE","expectedValue":10}` |
| `Check.config.totalColumnCount.columnCountConfig.expectedValue` | string | 是 | 示例: `10` |
| `Check.config.totalColumnCount.columnCountConfig.severity` | enum | 是 | The severity level of the check. Possible values are MODERATE or CRITICAL.<br>示例: `MODERATE` |
| `Check.config.numericColumnMedian` | object | 否 | Checks the median value of a numeric column. |
| `Check.config.numericColumnMedian.subject` | object | 是 | A dataset resource type.<br>示例: `{"datasetRid":"ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da","branchId":"master"}` |
| `Check.config.numericColumnMedian.subject.datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `Check.config.numericColumnMedian.subject.branchId` | string | 是 | The name of a Branch.<br>示例: `master` |
| `Check.config.numericColumnMedian.numericColumnCheckConfig` | object | 是 | Configuration for numeric column-based checks (such as mean or median). At least one of numericBounds or trend must be specified. Both may be provided to validate both the absolute value range and the trend behavior over time.<br>示例: `{"numericBounds":{"severity":"MODERATE","numericBounds":{"upperBound":1000.0,"lowerBound":0.0}},"trend":{"severity":"MODERATE","differenceBounds":{"upperBound":1000.0,"lowerBound":0.0},"trendType":"NON_DECREASING"},"columnName":"amount"}` |
| `Check.config.numericColumnMedian.numericColumnCheckConfig.columnName` | string | 是 | 示例: `amount` |
| `Check.config.numericColumnMedian.numericColumnCheckConfig.numericBounds` | object | 否 | Configuration for numeric bounds check with severity settings.<br>示例: `{"severity":"MODERATE","numericBounds":{"upperBound":1000.0,"lowerBound":0.0}}` |
| `Check.config.numericColumnMedian.numericColumnCheckConfig.numericBounds.numericBounds` | object | 是 | The range of numeric values a check is expected to be within.<br>示例: `{"upperBound":1000.0,"lowerBound":0.0}` |
| `Check.config.numericColumnMedian.numericColumnCheckConfig.numericBounds.numericBounds.lowerBound` | number | 否 | 示例: `0.0` |
| `Check.config.numericColumnMedian.numericColumnCheckConfig.numericBounds.numericBounds.upperBound` | number | 否 | 示例: `1000.0` |
| `Check.config.numericColumnMedian.numericColumnCheckConfig.numericBounds.severity` | enum | 是 | The severity level of the check. Possible values are MODERATE or CRITICAL.<br>示例: `MODERATE` |
| `Check.config.numericColumnMedian.numericColumnCheckConfig.trend` | object | 否 | Configuration for trend-based validation with severity settings. At least one of trendType or differenceBounds must be specified. Both may be provided to validate both the trend pattern and the magnitude of change.<br>示例: `{"severity":"MODERATE","differenceBounds":{"upperBound":1000.0,"lowerBound":0.0},"trendType":"NON_DECREASING"}` |
| `Check.config.numericColumnMedian.numericColumnCheckConfig.trend.trendType` | enum | 否 | The type of trend to validate:<br>- NON_INCREASING: Values should not increase over time<br>- NON_DECREASING: Values should not decrease over time<br>- STRICTLY_INCREASING: Values should strictly increase over time<br>- STRICTLY_DECREASING: Values should strictly decrease over time<br>- CONSTANT: Values should remain constant over time<br>示例: `NON_DECREASING` |
| `Check.config.numericColumnMedian.numericColumnCheckConfig.trend.differenceBounds` | object | 否 | The range of numeric values a check is expected to be within.<br>示例: `{"upperBound":1000.0,"lowerBound":0.0}` |
| `Check.config.numericColumnMedian.numericColumnCheckConfig.trend.differenceBounds.lowerBound` | number | 否 | 示例: `0.0` |
| `Check.config.numericColumnMedian.numericColumnCheckConfig.trend.differenceBounds.upperBound` | number | 否 | 示例: `1000.0` |
| `Check.config.numericColumnMedian.numericColumnCheckConfig.trend.severity` | enum | 是 | The severity level of the check. Possible values are MODERATE or CRITICAL.<br>示例: `MODERATE` |
| `Check.config.buildDuration` | object | 否 | Checks the total time a build takes to complete. |
| `Check.config.buildDuration.subject` | object | 是 | A dataset resource type.<br>示例: `{"datasetRid":"ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da","branchId":"master"}` |
| `Check.config.buildDuration.subject.datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `Check.config.buildDuration.subject.branchId` | string | 是 | The name of a Branch.<br>示例: `master` |
| `Check.config.buildDuration.timeCheckConfig` | object | 是 | 示例: `{"timeBounds":{"severity":"MODERATE","timeBounds":{"lowerBoundInSeconds":3600,"upperBoundInSeconds":3600}},"medianDeviation":{"severity":"MODERATE","medianDeviation":{"deviationThreshold":2.0,"dataPoints":10,"boundsType":"TWO_TAILED"}}}` |
| `Check.config.buildDuration.timeCheckConfig.timeBounds` | object | 否 | Configuration for time bounds check with severity settings.<br>示例: `{"severity":"MODERATE","timeBounds":{"lowerBoundInSeconds":3600,"upperBoundInSeconds":3600}}` |
| `Check.config.buildDuration.timeCheckConfig.timeBounds.timeBounds` | object | 是 | The configuration for the range of time between which the health check is expected to succeed.<br>示例: `{"lowerBoundInSeconds":3600,"upperBoundInSeconds":3600}` |
| `Check.config.buildDuration.timeCheckConfig.timeBounds.timeBounds.lowerBoundInSeconds` | string | 否 | 示例: `3600` |
| `Check.config.buildDuration.timeCheckConfig.timeBounds.timeBounds.upperBoundInSeconds` | string | 否 | 示例: `3600` |
| `Check.config.buildDuration.timeCheckConfig.timeBounds.severity` | enum | 是 | The severity level of the check. Possible values are MODERATE or CRITICAL.<br>示例: `MODERATE` |
| `Check.config.buildDuration.timeCheckConfig.medianDeviation` | object | 否 | Configuration for median deviation check with severity settings.<br>示例: `{"severity":"MODERATE","medianDeviation":{"deviationThreshold":2.0,"dataPoints":10,"boundsType":"TWO_TAILED"}}` |
| `Check.config.buildDuration.timeCheckConfig.medianDeviation.medianDeviation` | object | 是 | The number of thresholds the build's duration differs from the median.<br>示例: `{"deviationThreshold":2.0,"dataPoints":10,"boundsType":"TWO_TAILED"}` |
| `Check.config.buildDuration.timeCheckConfig.medianDeviation.medianDeviation.boundsType` | enum | 否 | The three types of median deviations a bounds type can have: - LOWER_BOUND â Tests for significant deviations below the median value, - UPPER_BOUND â Tests for significant deviations above the median value, - TWO_TAILED â Tests for significant deviations in either direction from the median value.<br>示例: `TWO_TAILED` |
| `Check.config.buildDuration.timeCheckConfig.medianDeviation.medianDeviation.dataPoints` | integer | 是 | 示例: `10` |
| `Check.config.buildDuration.timeCheckConfig.medianDeviation.medianDeviation.deviationThreshold` | number | 是 | 示例: `2.0` |
| `Check.config.buildDuration.timeCheckConfig.medianDeviation.severity` | enum | 是 | The severity level of the check. Possible values are MODERATE or CRITICAL.<br>示例: `MODERATE` |
| `Check.config.schemaComparison` | object | 否 | Checks the dataset schema against an expected schema. |
| `Check.config.schemaComparison.subject` | object | 是 | A dataset resource type.<br>示例: `{"datasetRid":"ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da","branchId":"master"}` |
| `Check.config.schemaComparison.subject.datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `Check.config.schemaComparison.subject.branchId` | string | 是 | The name of a Branch.<br>示例: `master` |
| `Check.config.schemaComparison.schemaComparisonConfig` | object | 是 | Configuration for schema comparison validation with severity settings.<br>示例: `{"severity":"MODERATE","schemaComparisonType":"EXACT_MATCH_ORDERED_COLUMNS","expectedSchema":{"columns":[{"columnType":"ARRAY","name":"column"}]}}` |
| `Check.config.schemaComparison.schemaComparisonConfig.expectedSchema` | object | 是 | Information about a dataset schema including all columns.<br>示例: `{"columns":[{"columnType":"ARRAY","name":"column"}]}` |
| `Check.config.schemaComparison.schemaComparisonConfig.expectedSchema.columns` | list<ColumnInfo> | 否 | — |
| `Check.config.schemaComparison.schemaComparisonConfig.expectedSchema.columns.ColumnInfo` | object | 是 | Information about a column including its name and type. |
| `Check.config.schemaComparison.schemaComparisonConfig.expectedSchema.columns.ColumnInfo.name` | string | 是 | 示例: `column` |
| `Check.config.schemaComparison.schemaComparisonConfig.expectedSchema.columns.ColumnInfo.columnType` | enum | 否 | The data type of a column in a dataset schema.<br>示例: `ARRAY` |
| `Check.config.schemaComparison.schemaComparisonConfig.schemaComparisonType` | enum | 是 | The type of schema comparison to perform:<br>- EXACT_MATCH_ORDERED_COLUMNS: Schemas must have identical columns in the same order.<br>- EXACT_MATCH_UNORDERED_COLUMNS: Schemas must have identical columns but order doesn't matter.<br>- COLUMN_ADDITIONS_ALLOWED: Expected schema columns must be present, additional columns are allowed and<br>missing column types are ignored.<br>- COLUMN_ADDITIONS_ALLOWED_STRICT: Expected schema columns must be present, additional columns are allowed.<br>Both expected and actual columns must specify types and they must match exactly.<br>示例: `EXACT_MATCH_ORDERED_COLUMNS` |
| `Check.config.schemaComparison.schemaComparisonConfig.severity` | enum | 是 | The severity level of the check. Possible values are MODERATE or CRITICAL.<br>示例: `MODERATE` |
| `Check.config.primaryKey` | object | 否 | Checks the uniqueness and non-null values of one or more columns (primary key constraint). |
| `Check.config.primaryKey.subject` | object | 是 | A dataset resource type.<br>示例: `{"datasetRid":"ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da","branchId":"master"}` |
| `Check.config.primaryKey.subject.datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `Check.config.primaryKey.subject.branchId` | string | 是 | The name of a Branch.<br>示例: `master` |
| `Check.config.primaryKey.primaryKeyConfig` | object | 是 | Configuration for primary key validation with severity settings.<br>示例: `{"severity":"MODERATE","columnNames":["user_id","account_id"]}` |
| `Check.config.primaryKey.primaryKeyConfig.columnNames` | list<ColumnName> | 否 | 示例: `["user_id","account_id"]` |
| `Check.config.primaryKey.primaryKeyConfig.columnNames.ColumnName` | string | 是 | — |
| `Check.config.primaryKey.primaryKeyConfig.severity` | enum | 是 | The severity level of the check. Possible values are MODERATE or CRITICAL.<br>示例: `MODERATE` |
| `Check.intent` | string | 否 | A note about why the Check was set up.<br>示例: `Check to ensure builds are passing.` |
| `Check.createdBy` | string | 否 | The user that created the Check.<br>示例: `f05f8da4-b84c-4fca-9c77-8af0b13d11de` |
| `Check.updatedTime` | string | 否 | The timestamp when the Check was last updated.<br>示例: `2024-09-25T17:29:35.974Z` |

```json
{
  "updatedTime": "2024-09-25T17:29:35.974Z",
  "createdBy": "f05f8da4-b84c-4fca-9c77-8af0b13d11de",
  "groups": [
    "ri.data-health.main.check-group.08e376a8-607d-4f44-b8dd-b4587be6ce9b"
  ],
  "rid": "ri.data-health.main.check.8e27b13a-e21b-4232-ae1b-76ccf5ff42b3",
  "intent": "Check to ensure builds are passing."
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| INVALID_ARGUMENT | `ModifyingCheckTypeNotSupported` | Changing the type of a check after it has been created is not supported. |
| INVALID_ARGUMENT | `InvalidTimeCheckConfig` | The TimeCheckConfig is invalid. It must contain at least one of timeBounds or medianDeviation. |
| INVALID_ARGUMENT | `InvalidPercentageCheckConfig` | The PercentageCheckConfig is invalid. It must contain at least one of percentageBounds or medianDeviation. |
| INVALID_ARGUMENT | `InvalidNumericColumnCheckConfig` | The NumericColumnCheckConfig is invalid. It must contain at least one of numericBounds or trend. |
| INVALID_ARGUMENT | `InvalidTrendConfig` | The TrendConfig is invalid. It must contain at least one of trendType or differenceBounds. |
| INVALID_ARGUMENT | `InvalidTransactionTimeCheckConfig` | The TransactionTimeCheckConfig is invalid. It must contain at least one of timeBounds or medianDeviation. |
| INVALID_ARGUMENT | `CheckTypeNotSupported` | The type of the requested check is not yet supported in the Platform API. |
| PERMISSION_DENIED | `ReplaceCheckPermissionDenied` | Could not replace the Check. |
| NOT_FOUND | `CheckNotFound` | The given Check could not be found. |
