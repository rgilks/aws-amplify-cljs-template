import {
  ModelInit,
  MutableModel,
  __modelMeta__,
  ManagedIdentifier
} from '@aws-amplify/datastore'
// @ts-ignore
import {
  LazyLoading,
  LazyLoadingDisabled,
  AsyncCollection
} from '@aws-amplify/datastore'

type EagerPlayer = {
  readonly [__modelMeta__]: {
    identifier: ManagedIdentifier<Player, 'id'>
  }
  readonly id: string
  readonly gameID: string
  readonly createdAt?: string | null
  readonly updatedAt?: string | null
  readonly owner?: string | null
}

type LazyPlayer = {
  readonly [__modelMeta__]: {
    identifier: ManagedIdentifier<Player, 'id'>
  }
  readonly id: string
  readonly gameID: string
  readonly createdAt?: string | null
  readonly updatedAt?: string | null
  readonly owner?: string | null
}

export declare type Player = LazyLoading extends LazyLoadingDisabled
  ? EagerPlayer
  : LazyPlayer

export declare const Player: (new (init: ModelInit<Player>) => Player) & {
  copyOf(
    source: Player,
    mutator: (draft: MutableModel<Player>) => MutableModel<Player> | void
  ): Player
}

type EagerGame = {
  readonly [__modelMeta__]: {
    identifier: ManagedIdentifier<Game, 'id'>
  }
  readonly id: string
  readonly name?: string | null
  readonly slug?: string | null
  readonly players?: (Player | null)[] | null
  readonly createdAt?: string | null
  readonly updatedAt?: string | null
  readonly owner?: string | null
}

type LazyGame = {
  readonly [__modelMeta__]: {
    identifier: ManagedIdentifier<Game, 'id'>
  }
  readonly id: string
  readonly name?: string | null
  readonly slug?: string | null
  readonly players: AsyncCollection<Player>
  readonly createdAt?: string | null
  readonly updatedAt?: string | null
  readonly owner?: string | null
}

export declare type Game = LazyLoading extends LazyLoadingDisabled
  ? EagerGame
  : LazyGame

export declare const Game: (new (init: ModelInit<Game>) => Game) & {
  copyOf(
    source: Game,
    mutator: (draft: MutableModel<Game>) => MutableModel<Game> | void
  ): Game
}
