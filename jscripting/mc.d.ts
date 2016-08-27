

declare type int = number;
declare type long = number;
declare type float = number;
declare type double = number;

declare namespace MC {

    interface UUID {
    }

    interface JavaList<T> {
        size(): int;
        get(index: int): T;
    }

    interface IChatComponent { }
    interface UserIdent { }
    interface Item { }

    interface Block {
        getName(): string;
    }

    interface World {
        getDimension(): int;
        getDifficulty(): int;
        getPlayerEntities(): JavaList<EntityPlayer>;
        blockExists(x: int, y: int, z: int): boolean;
        getBlock(x: int, y: int, z: int): Block;
    }

    interface WorldServer extends World {
    }

    interface Point {
        getX(): int;
        getY(): int;
        getZ(): int;
        setX(x: int): Point;
        setY(y: int): Point;
        setZ(z: int): Point;
    }

    interface WorldPoint {
        getDimension(): int;
        setDimension(dim: int): WorldPoint;
        setX(x: int): WorldPoint;
        setY(y: int): WorldPoint;
        setZ(z: int): WorldPoint;
    }

    interface WorldZone {
    }

    interface ICommandSender {
        getName(): string;

        doAs(userId: UUID, hideChatOutput: boolean): ICommandSender;
        doAs(player: EntityPlayer, hideChatOutput: boolean): ICommandSender;

        chatConfirm(message: string): void;
        chatNotification(message: string): void;
        chatWarning(message: string): void;
        chatError(message: string): void;
    }

    interface Entity {
        getId(): string;
        getUuid(): UUID;
        getName(): string;
        getEntityId(): int;

        getDimension(): int;
        getX(): double;
        getY(): double;
        getZ(): double;
        getMotionX(): double;
        getMotionY(): double;
        getMotionZ(): double;
        getChunkCoordX(): int;
        getChunkCoordY(): int;
        getChunkCoordZ(): int;

        getWidth(): float;
        getHeight(): float;
        getStepHeight(): float;
        isOnGround(): boolean;

        getRidingEntity(): Entity;
        getRiddenByEntity(): Entity;
        getWorld(): World;
    }

    interface EntityLivingBase extends Entity {
        getHealth(): float;
        setHealth(value: float);
        getMaxHealth(): float;
        getTotalArmorValue(): int;

        canEntityBeSeen(other: Entity): boolean;
    }

    interface EntityPlayer extends EntityLivingBase {
        setPosition(x: double, y: double, z: double): void;
        setPosition(x: double, y: double, z: double, yaw: float, pitch: float): void;
    }

    interface CommandParserArgs {
        isTabCompletion: boolean;
        // command: ICommand;
        // args: Queue<String>;
        // sender: ICommandSender;
        // senderPlayer: EntityPlayerMP;
        // ident: UserIdent;
        // permissionContext: PermissionContext;
        sendMessage(message: IChatComponent): void;
        confirm(message: string, ...args: any[]): void;
        notify(message: string, ...args: any[]): void;
        warn(message: string, ...args: any[]): void;
        error(message: string, ...args: any[]): void;
        size(): int;
        remove(): string;
        peek(): string;
        isEmpty(): boolean;
        hasPlayer(): boolean;
        parsePlayer(mustExist: boolean, mustBeOnline: boolean): UserIdent;
        parseItem(): Item;
        parseBlock(): Block;
        parsePermission(): string;
        checkPermission(perm: string): void;
        hasPermission(perm: string): boolean;
        tabComplete(...completionList: string[]): void;
        tabCompleteWord(completion: string): void;
        parseWorld(): WorldServer;
        parseInt(): int;
        parseInt(min: int, max: int): int;
        parseLong(): long;
        parseDouble(): double;
        parseBoolean(): boolean;
        parseTimeReadable(): long;
        checkTabCompletion(): void;
        requirePlayer(): void;
        toArray(): string[];
        tostring(): string;
        getSenderPoint(): WorldPoint;
        getWorldZone(): WorldZone;
        needsPlayer(): void;
    }

    interface BlockStatic {
        getBlockFromName(name: string): Block;
    }

    // interface WorldStatic {
    //     getWorld(dim: int): World;
    // }

    interface ServerStatic {
        runCommand(sender: ICommandSender, cmd: string, ...args: any[]): ICommandSender;

        chatConfirm(message: string): void;
        chatNotification(message: string): void;
        chatWarning(message: string): void;
        chatError(message: string): void;
    }
}

declare var Block: MC.BlockStatic;

// declare var World: MC.WorldStatic;

declare var Server: MC.ServerStatic;

/**
 * player - can be null!
 */
declare var player: MC.EntityPlayer;

/**
 * sender - should never be null (maybe if script was triggered by server only?)
 */
declare var sender: MC.ICommandSender;

/**
 * Command arguments - can be null!
 */
declare var args: MC.CommandParserArgs;