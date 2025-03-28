package com.cometproject.server.game.rooms.objects.items;

import com.cometproject.api.game.furniture.types.FurnitureDefinition;
import com.cometproject.api.game.rooms.objects.data.LimitedEditionItemData;
import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.items.types.DefaultFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.DefaultWallItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.*;
import com.cometproject.server.game.rooms.objects.items.types.floor.academy.GymEquipmentFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.academy.IceSkateFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.academy.RollerSkateFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.academy.SkateRailFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.boutique.MannequinFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.football.*;
import com.cometproject.server.game.rooms.objects.items.types.floor.games.banzai.*;
import com.cometproject.server.game.rooms.objects.items.types.floor.games.battleball.BattleBallTileFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.games.freeze.*;
import com.cometproject.server.game.rooms.objects.items.types.floor.groups.GroupFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.groups.GroupGateFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.hollywood.HaloTileFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.pet.PetFoodFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.pet.PetNestFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.pet.PetToyFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.pet.breeding.types.*;
import com.cometproject.server.game.rooms.objects.items.types.floor.pet.eggs.PterosaurEggFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.pet.eggs.VelociraptorEggFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.pet.horse.HorseJumpFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.pet.plants.MonsterPlantSeedFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.snowboarding.SnowboardJumpFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.snowboarding.SnowboardSlopeFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.summer.SummerShowerFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.totem.TotemBodyFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.totem.TotemHeadFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.totem.TotemPlanetFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions.*;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions.custom.*;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.addons.*;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.custom.WiredConditionSuperWired;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.negative.*;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.negative.custom.*;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive.*;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive.custom.*;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.highscore.HighscoreClassicFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.highscore.HighscoreMostWinsFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.highscore.HighscorePerTeamFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.*;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.custom.*;
import com.cometproject.server.game.rooms.objects.items.types.wall.MoodlightWallItem;
import com.cometproject.server.game.rooms.objects.items.types.wall.PostItWallItem;
import com.cometproject.server.game.rooms.objects.items.types.wall.WheelWallItem;
import com.cometproject.server.game.rooms.types.Room;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class RoomItemFactory {
    public static final String STACK_TOOL = "tile_stackmagic";
    public static final String TELEPORT_PAD = "teleport_pad";
    private static final int processMs = 500;
    private static final String GIFT_DATA = "GIFT::##";
    private static final Logger log = LogManager.getLogger(RoomItemFactory.class.getName());

    private static final Map<String, Class<? extends RoomItemFloor>> itemDefinitionMap;
    private static final Map<String, Constructor<? extends RoomItemFloor>> itemConstructorCache;

    static {
        itemConstructorCache = new ConcurrentHashMap<>();

        itemDefinitionMap = new HashMap<>() {{
            put("roller", RollerFloorItem.class);
            put("dice", DiceFloorItem.class);
            put("teleport", TeleporterFloorItem.class);
            put("teleport_door", TeleporterFloorItem.class);
            put("teleport_pad", TeleportPadFloorItem.class);
            put("onewaygate", OneWayGateFloorItem.class);
            put("gate", GateFloorItem.class);
            put("roombg", BackgroundTonerFloorItem.class);
            put("bed", BedFloorItem.class);
            put("vendingmachine", VendingMachineFloorItem.class);
            put("sideless_vending_machine", SidelessVendingMachineFloorItem.class);
            put("fx_vendingmachine", EffectVendingMachineFloorItem.class);
            put("mannequin", MannequinFloorItem.class);
            put("beach_shower", SummerShowerFloorItem.class);
            put("halo_tile", HaloTileFloorItem.class);
            put("adjustable_height_seat", AdjustableHeightSeatFloorItem.class);
            put("multiheight", AdjustableHeightFloorItem.class);
            put("lovelock", LoveLockFloorItem.class);
            put("soundmachine", SoundMachineFloorItem.class);
            put("privatechat", PrivateChatFloorItem.class);
            put("badge_display", BadgeDisplayFloorItem.class);
            put("piano", PianoFloorItem.class);
            put("slots_machine", SlotMachineFloorItem.class);

            put("wf_act_flee", WiredActionFlee.class);
            put("wf_act_match_to_sshot", WiredActionMatchToSnapshot.class);
            put("wf_act_teleport_to", WiredActionTeleportPlayerNew.class);
            put("wf_act_show_message", WiredActionShowMessage.class);
            put("wf_act_toggle_state", WiredActionToggleState.class);
            put("wf_act_give_reward", WiredActionGiveReward.class);
            put("wf_act_move_rotate", WiredActionMoveRotate.class);
            put("wf_act_chase", WiredActionChase.class);
            put("wf_act_kick_user", WiredActionKickUser.class);
            put("wf_act_reset_timers", WiredActionResetTimers.class);
            put("wf_act_join_team", WiredActionJoinTeam.class);
            put("wf_act_leave_team", WiredActionLeaveTeam.class);
            put("wf_act_give_score", WiredActionGiveScore.class);
            put("wf_act_give_score_tm", WiredActionGiveScoreTeam.class);
            put("wf_act_bot_talk", WiredActionBotTalk.class);
            put("wf_act_bot_give_handitem", WiredActionBotGiveHandItem.class);
            put("wf_act_bot_move", WiredActionBotMove.class);
            put("wf_act_bot_teleport", WiredActionBotTeleport.class);
            put("wf_act_comet", WiredActionComet.class);
            put("wf_act_move_to_dir", WiredActionMoveToDirection.class);
            put("wf_act_bot_talk_to_avatar", WiredActionBotTalkToAvatar.class);
            put("wf_act_bot_clothes", WiredActionBotClothes.class);
            put("wf_act_bot_follow_avatar", WiredActionBotFollowAvatar.class);
            put("wf_act_call_stacks", WiredActionExecuteStacks.class);
            put("wf_act_increment_timers", WiredActionIncrementTimers.class);

            put("wf_trg_says_something", WiredTriggerPlayerSaysKeyword.class);
            put("wf_trg_says_sycommand", WiredTriggerUserSaysCommand.class);
            put("wf_trg_enter_room", WiredTriggerEnterRoom.class);
            put("wf_trg_periodically", WiredTriggerPeriodically.class);
            put("wf_trg_walks_off_furni", WiredTriggerWalksOffFurni.class);
            put("wf_trg_walks_on_furni", WiredTriggerWalksOnFurni.class);
            put("wf_trg_state_changed", WiredTriggerStateChanged.class);
            put("wf_trg_game_starts", WiredTriggerGameStarts.class);
            put("wf_trg_game_ends", WiredTriggerGameEnds.class);
            put("wf_trg_collision", WiredTriggerCollision.class);
            put("wf_trg_period_long", WiredTriggerPeriodicallyLong.class);
            put("wf_trg_at_given_time", WiredTriggerAtGivenTime.class);
            put("wf_trg_at_given_time_long", WiredTriggerAtGivenTimeLong.class);
            put("wf_trg_score_achieved", WiredTriggerScoreAchieved.class);
            put("wf_trg_bot_reached_avtr", WiredTriggerBotReachedAvatar.class);

            put("wf_cnd_trggrer_on_frn", WiredConditionTriggererOnFurni.class);
            put("wf_cnd_not_trggrer_on", WiredNegativeConditionTriggererOnFurni.class);
            put("wf_cnd_actor_in_group", WiredConditionPlayerInGroup.class);
            put("wf_cnd_not_in_group", WiredNegativeConditionPlayerInGroup.class);
            put("wf_cnd_furnis_hv_avtrs", WiredConditionFurniHasPlayers.class);
            put("wf_cnd_not_hv_avtrs", WiredNegativeConditionFurniHasPlayers.class);
            put("wf_cnd_wearing_badge", WiredConditionPlayerHasBadgeEquipped.class);
            put("wf_cnd_not_wearing_badge", WiredNegativeConditionPlayerHasBadgeEquipped.class);
            put("wf_cnd_wearing_effect", WiredConditionPlayerWearingEffect.class);
            put("wf_cnd_not_wearing_effect", WiredNegativeConditionPlayerWearingEffect.class);
            put("wf_cnd_has_furni_on", WiredConditionHasFurniOn.class);
            put("wf_cnd_not_furni_on", WiredNegativeConditionHasFurniOn.class);
            put("wf_cnd_user_count_in", WiredConditionPlayerCountInRoom.class);
            put("wf_cnd_not_user_count", WiredConditionPlayerCountInRoom.class);
            put("wf_cnd_match_snapshot", WiredConditionMatchSnapshot.class);
            put("wf_cnd_not_match_snap", WiredNegativeConditionMatchSnapshot.class);
            put("wf_cnd_has_handitem", WiredConditionHasHandItem.class);
            put("wf_cnd_not_handitem", WiredNegativeConditionHasHandItem.class);
            put("wf_cnd_time_more_than", WiredConditionTimeMoreThan.class);
            put("wf_cnd_time_less_than", WiredConditionTimeLessThan.class);
            put("wf_cnd_actor_in_team", WiredConditionPlayerInTeam.class);
            put("wf_cnd_not_in_team", WiredNegativeConditionPlayerInTeam.class);
            put("wf_cnd_stuff_is", WiredConditionStuffIs.class);
            put("wf_cnd_not_stuff_is", WiredNegativeConditionStuffIs.class);

            put("wf_xtra_unseen", WiredAddonUnseenEffect.class);
            put("wf_xtra_random", WiredAddonRandomEffect.class); // new

            put("wf_floor_switch1", WiredAddonFloorSwitch.class);
            put("wf_floor_switch2", WiredAddonFloorSwitch.class);
            put("wf_colorwheel", WiredAddonColourWheel.class);
            put("wf_pressureplate", WiredAddonPressurePlate.class);
            put("wf_arrowplate", WiredAddonPressurePlate.class);
            put("wf_ringplate", WiredAddonPressurePlate.class);
            put("wf_pyramid", WiredAddonPyramid.class);
            put("wf_visual_timer", WiredAddonVisualTimer.class);
            put("wf_blob", WiredAddonBlob.class);
            put("wf_puzzlebox", WiredAddonNewPuzzleBox.class);

            put("wf_act_position_collision", WiredCustomCollisionPosition.class);
            put("wf_act_collision_case", WiredCustomCollisionCase.class);
            put("wf_act_unfreeze", WiredCustomUnfreeze.class);
            put("wf_cstm_set_speed", WiredCustomSetSpeed.class);

            put("slot_machine", SlotMachineFloorItem.class); // slot machine
            put("slot_machine_credits", SlotMachineCreditsFloorItem.class); // slot machine credits
            put("slot_machine_duckets", SlotMachineDucketsFloorItem.class); // slot machine duckets
            put("wf_cstm_give_badge", WiredCustomGiveBadge.class); // Wired custom
            put("wf_cnd_date_rng_active", WiredConditionDateRange.class); //finished wired
            put("wf_cstm_toggle_state_negative", WiredCustomToggleStateNegative.class); // mutar negativof
            put("wf_cstm_toggle_state_random", WiredCustomToggleStateRandom.class); // mutar aleatorio
            put("wf_cstm_show_message_room", WiredCustomShowMessageRoom.class); // mensaje a sala
            put("wf_cstm_mute_triggerer_user", WiredCustomMuteTriggerer.class); // mutear
            put("wf_cstm_tele_nfx_to", WiredCustomTeleportNoEffect.class); // teleport sin efecto
            put("wf_cstm_keyword_exclude", WiredTriggerCustomKeywordExclude.class);
            put("wf_cstm_keyword_not_is", WiredTriggerCustomKeywordNotIs.class);
            put("wf_cstm_add_tag", WiredCustomAddTag.class);
            put("wf_cstm_teleport_item", WiredCustomTeleportItem.class);
            put("wf_cstm_teleport_item_front", WiredCustomTeleportFront.class);
            put("wf_cstm_remove_tag", WiredCustomRemoveTag.class);
            put("wf_cstm_close_dices", WiredCustomCloseDices.class);
            put("wf_cstm_trg_starts_dancing", WiredTriggerCustomStartsDancing.class);
            put("wf_cstm_show_video_youtube", WiredCustomShouVideoYoutube.class);
            put("wf_cstm_cnd_has_rights", WiredConditionCustomHasRights.class);
            put("wf_cstm_cnd_ngt_has_rights", WiredNegativeConditionCustomHasRights.class);
            put("wf_cstm_reset_timers_afk", WiredCustomResetTimerAfk.class);
            put("wf_cstm_give_credits", WiredCustomGiveCredits.class);
            put("wf_cstm_give_activity_points", WiredCustomGiveActivityPoints.class);
            put("wf_cstm_give_vip_points", WiredCustomGiveVipPoints.class);
            put("wf_xtra_no_effect_items", WiredAddonNoItemsAnimateEffect.class);
            put("wf_cstm_move_and_rotate_base", WiredActionMoveRotateBase.class);
            put("wf_cstm_tridirectional", WiredActionSlideItem.class);
            put("wf_cstm_user_move", WiredCustomUserMove.class);
            put("wf_cstm_teleport_red", WiredCustomTeleportRed.class); // teleport ted
            put("wf_cstm_teleport_green", WiredCustomTeleportGreen.class); // teleport green
            put("wf_cstm_teleport_yellow", WiredCustomTeleportYellow.class); // teleport yellow
            put("wf_cstm_teleport_blue", WiredCustomTeleportBlue.class); // teleport blue
            put("wf_cstm_teleport_all", WiredCustomTeleportPlayerAll.class); // teleport all
            put("crafting", CraftingMachineFloorItem.class);
            put("wf_cnd_cstm_super_wired", WiredConditionSuperWired.class); // super wired
            put("arrow_move", ArrowFloorItem.class); // work
            put("wf_cstm_state_changed", WiredTriggerCustomStateChanged.class); // mutar de lejos
            put("wf_cstm_actions_player", WiredCustomActionsPlayer.class); // acciones en usuario
            put("wf_cstm_condition_has_player_red", WiredConditionCustomHasPlayerRed.class); // sobre un furni red
            put("wf_cstm_condition_has_player_blue", WiredConditionCustomHasPlayerBlue.class); // sobre un furni blue
            put("wf_cstm_condition_has_player_green", WiredConditionCustomHasPlayerGreen.class); // sobre un furni green
            put("wf_cstm_condition_has_player_yellow", WiredConditionCustomHasPlayerYellow.class); // sobre un furni yellow
            put("wf_cstm_reset_highscore", WiredCustomResetHighscore.class); // resetear puntuaciones
            put("wf_cnd_cstm_triggerer_furni_green", WiredConditionTriggererOnFurniGreen.class); // new wired
            put("wf_cnd_cstm_triggerer_furni_yellow", WiredConditionTriggererOnFurniYellow.class); // new wired
            put("wf_cnd_cstm_triggerer_furni_blue", WiredConditionTriggererOnFurniBlue.class); // new wired
            put("wf_cnd_cstm_triggerer_furni_red", WiredConditionTriggererOnFurniRed.class); // new wired
            put("wf_trg_cstm_sensor_movement", WiredTriggerCustomSensorMovement.class); // sensor de movimiento
            put("wf_trg_cls_player", WiredTriggerCollisionPlayer.class); // collision player 1
            put("wf_trg_cls_player_with2", WiredTriggerCollisionPlayerWithPlayer2.class); // collision player afectado
            put("wf_cstm_rotation_player", WiredCustomRotationPlayer.class); // rotar usuario
            put("wf_cnd_player_freeze", WiredConditionPlayerFreeze.class); // usuario está congelado
            put("wf_cnd_ngt_player_freeze", WiredNegativeConditionPlayerFreeze.class); // usuario no está congelado
            put("wf_cstm_handitem_bot", WiredCustomHanditemBot.class); // handitem bot
            put("wf_cstm_enable_bot", WiredCustomEnableBot.class); // enable bot
            put("wf_cstm_dance_bot", WiredCustomDanceBot.class); // dance bot
            put("wf_cstm_freeze_bot", WiredCustomFreezeBot.class); // freeze bot
            put("wf_xtra_tiles", WiredAddonTile.class); // tile de colores cambia de estado al pisarla
            put("monsterplant_seed", MonsterPlantSeedFloorItem.class);
            put("battle_ball_tile", BattleBallTileFloorItem.class);

            /* continue down */

            put("wf_cstm_set_points", WiredCustomSetPoints.class); // setear puntos
            put("wf_xtra_keb_bar", WiredAddonKebBar.class); // keb bar
            put("wf_trg_bot_reached_furni", WiredTriggerBotReachedFurni.class); // bot reached furni
            put("wf_trg_ex_command_reverse", WiredTriggerUserSaysCommandReversed.class); // usuario dice comando

            put("wf_xtra_or", WiredAddonOrEffect.class); // complemento OR
            put("wf_trg_anti_walk", WiredTriggerAntiWalk.class); // afk wired new
            put("wf_cstm_execute_stacks_conditions", WiredCustomExecuteStacksConditions.class); // ejecuta pilas con condiciones
            put("wf_cstm_force_collision_center", WiredCustomForceCollisionNoCenter.class); // caza solo en centro no alrededor
            put("wf_cstm_super_wired", WiredCustomSuperWired.class); // superwired efecto

            put("wf_trg_leave_room", WiredTriggerLeavesRoom.class); // trigger

            put("wf_cstm_freeze", WiredCustomFreeze.class); // action
            put("wf_cstm_fswalk", WiredCustomFastWalk.class); // action
            put("wf_cstm_dancee", WiredCustomDance.class); // action
            put("wf_cstm_enable", WiredCustomEnable.class); // action
            put("wf_cstm_hnitem", WiredCustomHanditem.class); // action
            put("wf_act_forwa", WiredCustomForwardRoom.class); // action
            put("wf_act_raise_furni", WiredCustomFurniUp.class); // action
            put("wf_act_lower_furni", WiredCustomFurniDown.class); // action
            put("wf_act_usr_clothes", WiredCustomChangeClothes.class); // action
            put("wf_act_tiles", WiredCustomForceCollision.class); // action
            put("wf_act_collisionteam", WiredCustomForceCollisionTeam.class); // action
            put("wf_act_endgame_team", WiredCustomTeamLoses.class); //action

            put("wf_cnd_habbo_has_diamonds", WiredConditionCustomHasDiamonds.class); // condition
            put("wf_cnd_not_habbo_has_diamonds", WiredNegativeConditionCustomHasDiamonds.class); // condition
            put("wf_cnd_habbo_has_duckets", WiredConditionCustomHasDuckets.class); // condition
            put("wf_cnd_not_habbo_has_duckets", WiredNegativeConditionCustomHasDuckets.class); // condition
            put("wf_cnd_habbo_has_diamondz", WiredConditionCustomHasDance.class); // condition
            put("wf_cnd_habbo_not_danc", WiredNegativeConditionCustomHasDance.class); // condition
            put("wf_cnd_habbo_has_rank", WiredConditionCustomHasRank.class); // condition
            put("wf_cnd_habbo_not_rank", WiredNegativeConditionCustomHasRank.class); // condition
            put("wf_cnd_actor_is_idley", WiredConditionCustomIsIdle.class); // condition
            put("wf_cnd_actor_is_idlen", WiredNegativeConditionCustomIsIdle.class); // condition
            put("wf_trg_afkkkdormeur", WiredTriggerCustomIdle.class);
            put("wf_trg_totalidle", WiredTriggerCustomTotalIdle.class);

            put("highscore_classic", HighscoreClassicFloorItem.class);
            put("highscore_perteam", HighscorePerTeamFloorItem.class);
            put("highscore_mostwins", HighscoreMostWinsFloorItem.class);

            put("pressureplate_seat", PressurePlateSeatFloorItem.class);

            put("bb_teleport", BanzaiTeleporterFloorItem.class);
            put("bb_red_gate", BanzaiGateFloorItem.class);
            put("bb_yellow_gate", BanzaiGateFloorItem.class);
            put("bb_blue_gate", BanzaiGateFloorItem.class);
            put("bb_green_gate", BanzaiGateFloorItem.class);
            put("bb_patch", BanzaiTileFloorItem.class);
            put("bb_timer", BanzaiTimerFloorItem.class);
            put("bb_puck", BanzaiPuckFloorItem.class);

            put("group_item", GroupFloorItem.class);
            put("group_forum", GroupFloorItem.class);
            put("group_gate", GroupGateFloorItem.class);

            put("football_timer", FootballTimerFloorItem.class);

            //Football Balls
            put("ball", FootballFloorItem.class);
            put("original_ball", OriginalFootballFloorItem.class);
            put("beta_football", BetaFootballFloorItem.class); // test
            put("beta_footballs", BetaRollableFloorItem.class);


            put("football_gate", FootballGateFloorItem.class);
            put("football_goal", FootballGoalFloorItem.class);
            put("football_score", FootballScoreFloorItem.class);

            put("generic_small_score", GenericSmallScoreFloorItem.class);
            put("generic_large_score", GenericLargeScoreFloorItem.class);

            put("snowb_slope", SnowboardSlopeFloorItem.class);
            put("snowb_rail", SnowboardJumpFloorItem.class);
            put("snowb_jump", SnowboardJumpFloorItem.class);

            put("totem_planet", TotemPlanetFloorItem.class);
            put("totem_head", TotemHeadFloorItem.class);
            put("totem_body", TotemBodyFloorItem.class);

            put("pet_toy", PetToyFloorItem.class);
            put("pet_food", PetFoodFloorItem.class);
            put("pet_nest", PetNestFloorItem.class);

            put("pterosaur_egg", PterosaurEggFloorItem.class);
            put("velociraptor_egg", VelociraptorEggFloorItem.class);

            put("breeding_dog", DogBreedingBoxFloorItem.class);
            put("breeding_cat", CatBreedingBoxFloorItem.class);
            put("breeding_pig", PigBreedingBoxFloorItem.class);
            put("breeding_terrier", TerrierBreedingBoxFloorItem.class);
            put("breeding_bear", BearBreedingBoxFloorItem.class);

            put("cannon", CannonFloorItem.class);

            put("horse_jump", HorseJumpFloorItem.class);

            put("water", WaterFloorItem.class);
            put("effect", EffectFloorItem.class);

            put("freeze_timer", FreezeTimerFloorItem.class);
            put("freeze_gate", FreezeGateFloorItem.class);
            put("freeze_tile", FreezeTileFloorItem.class);
            put("freeze_block", FreezeBlockFloorItem.class);
            put("freeze_exit", FreezeExitFloorItem.class);

            put("clothing", ClothingFloorItem.class);
            put("crackable", CrackableFloorItem.class);
            put("random_state", RandomStateFloorItem.class);

            // Custom's
            put("gym_equipment", GymEquipmentFloorItem.class);
            put("info_terminal", InfoTerminalFloorItem.class);

            put("iceskate", IceSkateFloorItem.class);
            put("rollerskate", RollerSkateFloorItem.class);
            put("skaterail", SkateRailFloorItem.class);

            put("move_stack_tool", MagicMoveFloorItem.class);

            put("wf_hideitem", WiredHideItem.class);
            put("wf_sit_hideitem", WiredSitHideItem.class);

            put("wf_act_cstm_freeze_uf", WiredCustomFreezeUnfreeze.class);
            put("wf_act_cstm_sit", WiredCustomActionSit.class);
            put("wf_act_cstm_lay", WiredCustomActionLay.class);
        }};
    }

    public static RoomItemFloor createFloor(RoomItemData itemData, Room room, FurnitureDefinition def) {
        RoomItemFloor floorItem = null;

        if (def == null) {
            return null;
        }

        if (def.canSit()) {
            floorItem = new SeatFloorItem(itemData, room);
        }

        if (def.getItemName().startsWith(STACK_TOOL)) {
            floorItem = new MagicStackFloorItem(itemData, room);
        }

        if (def.isAdFurni()) {
            floorItem = new AdsFloorItem(itemData, room);
        }

        if (def.getItemName().contains("yttv")) {
            floorItem = new VideoPlayerFloorItem(itemData, room);
        }

        if (itemData.getData().startsWith(GIFT_DATA)) {
            try {
                floorItem = new GiftFloorItem(itemData, room);
            } catch (Exception e) {
                return null;
            }
        } else {
            if (itemDefinitionMap.containsKey(def.getInteraction())) {
                try {
                    Constructor<? extends RoomItemFloor> constructor;

                    if (itemConstructorCache.containsKey(def.getInteraction())) {
                        constructor = itemConstructorCache.get(def.getInteraction());
                    } else {
                        constructor = itemDefinitionMap.get(def.getInteraction()).getConstructor(RoomItemData.class, Room.class);
                        itemConstructorCache.put(def.getInteraction(), constructor);
                    }

                    if (constructor != null)
                        floorItem = constructor.newInstance(itemData, room);
                } catch (Exception e) {
                    log.warn("Failed to create instance for item: " + itemData.getId() + ", type: " + def.getInteraction(), e);
                }
            }
        }


        if (floorItem == null) {
            floorItem = new DefaultFloorItem(itemData, room);
        }

        if (itemData.getLimitedEdition() != null) {
            floorItem.setLimitedEditionItemData((LimitedEditionItemData) itemData.getLimitedEdition());
        }

        return floorItem;
    }

    public static RoomItemWall createWall(RoomItemData itemData, Room room, FurnitureDefinition def) {
        if (def == null) {
            return null;
        }

        RoomItemWall wallItem;

        switch (def.getInteraction()) {
            case "habbowheel" -> wallItem = new WheelWallItem(itemData, room);
            case "dimmer" -> wallItem = new MoodlightWallItem(itemData, room);
            case "postit" -> wallItem = new PostItWallItem(itemData, room);
            default -> wallItem = new DefaultWallItem(itemData, room);
        }

        if (itemData.getLimitedEdition() != null) {
            wallItem.setLimitedEditionItemData((LimitedEditionItemData) itemData.getLimitedEdition());
        }

        return wallItem;
    }

    public static int getProcessTime(double time) {
        long realTime = Math.round(time * 1000 / processMs);

        if (realTime < 1) {
            realTime = 0; //0.5s
        }

        return (int) realTime;
    }
}
