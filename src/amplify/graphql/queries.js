/* eslint-disable */
// this is an auto generated file. This will be overwritten

export const getPlayer = /* GraphQL */ `
  query GetPlayer($id: ID!) {
    getPlayer(id: $id) {
      id
      gameID
      createdAt
      updatedAt
      owner
      _version
      _deleted
      _lastChangedAt
    }
  }
`
export const listPlayers = /* GraphQL */ `
  query ListPlayers(
    $filter: ModelPlayerFilterInput
    $limit: Int
    $nextToken: String
  ) {
    listPlayers(filter: $filter, limit: $limit, nextToken: $nextToken) {
      items {
        id
        gameID
        createdAt
        updatedAt
        owner
        _version
        _deleted
        _lastChangedAt
      }
      nextToken
      startedAt
    }
  }
`
export const syncPlayers = /* GraphQL */ `
  query SyncPlayers(
    $filter: ModelPlayerFilterInput
    $limit: Int
    $nextToken: String
    $lastSync: AWSTimestamp
  ) {
    syncPlayers(
      filter: $filter
      limit: $limit
      nextToken: $nextToken
      lastSync: $lastSync
    ) {
      items {
        id
        gameID
        createdAt
        updatedAt
        owner
        _version
        _deleted
        _lastChangedAt
      }
      nextToken
      startedAt
    }
  }
`
export const playersByGameID = /* GraphQL */ `
  query PlayersByGameID(
    $gameID: ID!
    $sortDirection: ModelSortDirection
    $filter: ModelPlayerFilterInput
    $limit: Int
    $nextToken: String
  ) {
    playersByGameID(
      gameID: $gameID
      sortDirection: $sortDirection
      filter: $filter
      limit: $limit
      nextToken: $nextToken
    ) {
      items {
        id
        gameID
        createdAt
        updatedAt
        owner
        _version
        _deleted
        _lastChangedAt
      }
      nextToken
      startedAt
    }
  }
`
export const getGame = /* GraphQL */ `
  query GetGame($id: ID!) {
    getGame(id: $id) {
      id
      name
      slug
      players {
        items {
          id
          gameID
          createdAt
          updatedAt
          owner
          _version
          _deleted
          _lastChangedAt
        }
        nextToken
        startedAt
      }
      createdAt
      updatedAt
      owner
      _version
      _deleted
      _lastChangedAt
    }
  }
`
export const listGames = /* GraphQL */ `
  query ListGames(
    $filter: ModelGameFilterInput
    $limit: Int
    $nextToken: String
  ) {
    listGames(filter: $filter, limit: $limit, nextToken: $nextToken) {
      items {
        id
        name
        slug
        players {
          nextToken
          startedAt
        }
        createdAt
        updatedAt
        owner
        _version
        _deleted
        _lastChangedAt
      }
      nextToken
      startedAt
    }
  }
`
export const syncGames = /* GraphQL */ `
  query SyncGames(
    $filter: ModelGameFilterInput
    $limit: Int
    $nextToken: String
    $lastSync: AWSTimestamp
  ) {
    syncGames(
      filter: $filter
      limit: $limit
      nextToken: $nextToken
      lastSync: $lastSync
    ) {
      items {
        id
        name
        slug
        players {
          nextToken
          startedAt
        }
        createdAt
        updatedAt
        owner
        _version
        _deleted
        _lastChangedAt
      }
      nextToken
      startedAt
    }
  }
`
