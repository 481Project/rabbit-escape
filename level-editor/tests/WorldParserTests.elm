module WorldParserTests exposing (all)

import Test exposing (describe,test,Test)
import Expect


import World exposing (World, Block(..), initWorld, makeBlockGrid, makeWorld)
import WorldParser exposing (parse)


all : Test
all =
    describe "Tests of the world parser and manipulation"
        [ test "Parse empty world" parseEmptyWorld
        ]


-- ---


okAndEqual : Result String a -> a -> () -> Expect.Expectation
okAndEqual actual expected =
    \() ->
        case actual of
            Ok value -> Expect.equal value expected
            Err error -> Expect.fail error


parseLines : String -> List String -> Result String World
parseLines comment strings =
    parse comment ((String.join "\n" strings) ++ "\n")


-- ---

parseEmptyWorld : () -> Expect.Expectation
parseEmptyWorld =
    okAndEqual
        (parseLines
            "tst"
            [ "   "
            , "   "
            , "   "
            ]
        )
        (makeWorld
            "tst"
            (makeBlockGrid
                [ [NoBlock, NoBlock, NoBlock]
                , [NoBlock, NoBlock, NoBlock]
                , [NoBlock, NoBlock, NoBlock]
                ]
            )
        )
