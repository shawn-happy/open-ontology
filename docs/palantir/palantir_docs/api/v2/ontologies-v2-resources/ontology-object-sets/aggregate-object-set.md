`POST /api/v2/ontologies/{ontology}/objectSets/aggregate`

Aggregates the ontology objects present in the `ObjectSet` from the provided object set definition.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:ontologies-read`.

**OAuth2 scopes**: `api:ontologies-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ontology` | string | 是 | The API name or RID of the Ontology. To find the API name or RID, use the **List Ontologies** endpoint or<br>check the **Ontology Manager**.<br>示例: `palantir` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `sdkPackageRid` | string | 否 | The package rid of the generated SDK. |
| `sdkVersion` | string | 否 | The package version of the generated SDK. |
| `branch` | string | 否 | The Foundry branch to aggregate the objects from. If not specified, the default branch is used.<br>Branches are an experimental feature and not all workflows are supported.<br>示例: `ri.branch..branch.d827184f-ee0e-4351-8b70-efbe51e07252` |
| `transactionId` | string | 否 | The ID of an Ontology transaction to read from.<br>Transactions are an experimental feature and all workflows may not be supported. |
| `scenarioRid` | string | 否 | The resource identifier of an ontology scenario to aggregate the objects on.<br>示例: `ri.actions..scenario.a1b2c3d4-e5f6-7890-abcd-ef1234567890` |
| `executeInMemoryOnly` | boolean | 否 | If true, the request fails with an error when it cannot be computed in-memory.<br>Use this to opt into fast failure on requests that would otherwise require<br>heavier computation.<br>Defaults to false. |

## Request body

```json
{
  "objectSet": {
    "objectType": "Employee",
    "type": "base"
  },
  "aggregation": [
    {
      "field": "tenure",
      "name": "min_tenure",
      "type": "min"
    },
    {
      "field": "tenure",
      "name": "avg_tenure",
      "type": "avg"
    }
  ],
  "groupBy": [
    {
      "field": "startDate",
      "ranges": [
        {
          "endValue": "2020-06-01",
          "startValue": "2020-01-01"
        }
      ],
      "type": "range"
    },
    {
      "field": "city",
      "type": "exact"
    }
  ]
}
```

## Response

**AggregateObjectsResponseV2**

Success response.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `AggregateObjectsResponseV2` | object | 是 | Success response.<br>示例: `{"data":[{"metrics":[{"name":"min_tenure","value":1},{"name":"avg_tenure","value":3}],"group":{"startDate":{"startValue":"2020-01-01","endValue":"2020-06-01"},"city":"New York City"}},{"metrics":[{"name":"min_tenure","value":2},{"name":"avg_tenure","value":3}],"group":{"startDate":{"startValue":"2020-01-01","endValue":"2020-06-01"},"city":"San Francisco"}}]}` |
| `AggregateObjectsResponseV2.excludedItems` | integer | 否 | — |
| `AggregateObjectsResponseV2.accuracy` | enum | 是 | — |
| `AggregateObjectsResponseV2.data` | list<AggregateObjectsResponseItemV2> | 否 | — |
| `AggregateObjectsResponseV2.data.AggregateObjectsResponseItemV2` | object | 是 | — |
| `AggregateObjectsResponseV2.data.AggregateObjectsResponseItemV2.group` | map | 否 | — |
| `AggregateObjectsResponseV2.data.AggregateObjectsResponseItemV2.group.AggregationGroupKeyV2` | string | 是 | — |
| `AggregateObjectsResponseV2.data.AggregateObjectsResponseItemV2.group.AggregationGroupValueV2` | any | 是 | — |
| `AggregateObjectsResponseV2.data.AggregateObjectsResponseItemV2.metrics` | list<AggregationMetricResultV2> | 否 | — |
| `AggregateObjectsResponseV2.data.AggregateObjectsResponseItemV2.metrics.AggregationMetricResultV2` | object | 是 | — |
| `AggregateObjectsResponseV2.data.AggregateObjectsResponseItemV2.metrics.AggregationMetricResultV2.name` | string | 是 | — |
| `AggregateObjectsResponseV2.data.AggregateObjectsResponseItemV2.metrics.AggregationMetricResultV2.value` | any | 否 | The value of the metric. This will be a double in the case of<br>a numeric metric, or a date string in the case of a date metric. |
| `AggregateObjectsResponseV2.computeUsage` | number | 否 | A measurement of compute usage expressed in [compute-seconds](/docs/foundry/resource-management/usage-types#compute-second). For more information, please refer to the [Usage types](/docs/foundry/resource-management/usage-types) documentation.<br>示例: `3.7` |

```json
{
  "data": [
    {
      "metrics": [
        {
          "name": "min_tenure",
          "value": 1
        },
        {
          "name": "avg_tenure",
          "value": 3
        }
      ],
      "group": {
        "startDate": {
          "startValue": "2020-01-01",
          "endValue": "2020-06-01"
        },
        "city": "New York City"
      }
    },
    {
      "metrics": [
        {
          "name": "min_tenure",
          "value": 2
        },
        {
          "name": "avg_tenure",
          "value": 3
        }
      ],
      "group": {
        "startDate": {
          "startValue": "2020-01-01",
          "endValue": "2020-06-01"
        },
        "city": "San Francisco"
      }
    }
  ]
}
```
