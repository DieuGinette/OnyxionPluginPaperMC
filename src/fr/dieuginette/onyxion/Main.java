package fr.dieuginette.onyxion;

/*     */ import java.util.HashMap;
/*     */ import java.util.logging.Level;
/*     */ import net.md_5.bungee.api.ChatColor;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.GameMode;
/*     */ import org.bukkit.GameRule;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.WorldCreator;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.block.BlockBreakEvent;
/*     */ import org.bukkit.event.block.BlockPlaceEvent;
/*     */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*     */ import org.bukkit.event.entity.EntityDamageEvent;
/*     */ import org.bukkit.event.entity.EntityRegainHealthEvent;
/*     */ import org.bukkit.event.entity.FoodLevelChangeEvent;
/*     */ import org.bukkit.event.entity.PlayerDeathEvent;
/*     */ import org.bukkit.event.player.PlayerDropItemEvent;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
/*     */ import org.bukkit.event.player.PlayerItemConsumeEvent;
/*     */ import org.bukkit.event.player.PlayerJoinEvent;
/*     */ import org.bukkit.event.player.PlayerRespawnEvent;
/*     */ import org.bukkit.event.player.PlayerTeleportEvent;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
/*     */ import org.bukkit.scheduler.BukkitRunnable;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Main
/*     */   extends JavaPlugin
/*     */   implements Listener
/*     */ {
/*     */   public DataManager data;
/*  42 */   public HashMap<Player, Long> lobbycooldown = new HashMap<>();
/*  43 */   public HashMap<Player, Long> lobbylastdamagecooldown = new HashMap<>();
/*     */   
/*     */   public Long saveCooldown;
/*  46 */   WorldCreator Onyxion = new WorldCreator(getConfig().getString("Lobby.World"));
/*  47 */   World lobby = null;
/*     */   
/*  49 */   int TimeBehavior = 0;
/*  50 */   int LobbyTime = 0;

/*     */   
/*     */   public void onEnable() {
/*  54 */     getServer().getPluginManager().registerEvents(this, (Plugin)this);
/*  55 */     saveDefaultConfig();
/*  56 */     this.data = new DataManager(this);
/*  57 */     this.lobby = Bukkit.createWorld(this.Onyxion);
/*  58 */     if (Bukkit.getWorld(getConfig().getString("Command.DefaultDestination.World")) == null) {
/*  59 */       Bukkit.getLogger().setLevel(Level.SEVERE);
/*  60 */       Bukkit.getLogger().severe("Monde par défaut non trouve !!! Veuillez définir le Command.DefaultDestination.World à un monde valide dans le config.yml du plugin et rechargez le plugin/serveur ! Desactiver le plugin...");
/*  61 */       Bukkit.getPluginManager().disablePlugin((Plugin)this);
/*     */     } 
/*  63 */     if (getConfig().getBoolean("Lobby.UseDefaultLobbyGamerules")) {
/*  64 */       Bukkit.getWorld(getConfig().getString("Lobby.World")).setGameRule(GameRule.DO_MOB_SPAWNING, Boolean.valueOf(false));
/*  65 */       Bukkit.getWorld(getConfig().getString("Lobby.World")).setGameRule(GameRule.DO_MOB_LOOT, Boolean.valueOf(false));
/*  66 */       Bukkit.getWorld(getConfig().getString("Lobby.World")).setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, Boolean.valueOf(false));
/*  67 */       Bukkit.getWorld(getConfig().getString("Lobby.World")).setGameRule(GameRule.NATURAL_REGENERATION, Boolean.valueOf(false));
/*  68 */       Bukkit.getWorld(getConfig().getString("Lobby.World")).setGameRule(GameRule.RANDOM_TICK_SPEED, Integer.valueOf(0));
/*  69 */       Bukkit.getWorld(getConfig().getString("Lobby.World")).setGameRule(GameRule.DO_TILE_DROPS, Boolean.valueOf(false));
/*     */     } 
/*  71 */     if (getConfig().isInt("Lobby.TimeBehavior")) {
/*  72 */       this.TimeBehavior = getConfig().getInt("Lobby.TimeBehavior");
/*  73 */       if (this.TimeBehavior == 1) {
/*  74 */         if (getConfig().getBoolean("Lobby.UseDefaultLobbyGamerules")) {
/*  75 */           Bukkit.getWorld(getConfig().getString("Lobby.World")).setGameRule(GameRule.DO_DAYLIGHT_CYCLE, Boolean.valueOf(false));
/*  76 */           Bukkit.getWorld(getConfig().getString("Lobby.World")).setGameRule(GameRule.DO_WEATHER_CYCLE, Boolean.valueOf(false));
/*     */         } 
/*  78 */         this.LobbyTime = getConfig().getInt("Lobby.LobbyTime");
/*  79 */         Bukkit.getWorld(getConfig().getString("Lobby.World")).setFullTime(this.LobbyTime);
/*  80 */       } else if (this.TimeBehavior == 2) {
/*  81 */         if (getConfig().getBoolean("Lobby.UseDefaultLobbyGamerules")) {
/*  82 */           Bukkit.getWorld(getConfig().getString("Lobby.World")).setGameRule(GameRule.DO_DAYLIGHT_CYCLE, Boolean.valueOf(true));
/*  83 */           Bukkit.getWorld(getConfig().getString("Lobby.World")).setGameRule(GameRule.DO_WEATHER_CYCLE, Boolean.valueOf(true));
/*     */         } 
/*  85 */         Bukkit.getWorld(getConfig().getString("Lobby.World")).setFullTime(Bukkit.getWorld(getConfig().getString("Command.DefaultDestination.World")).getFullTime());
/*  86 */         final World lobbyWorldInstance = Bukkit.getWorld(getConfig().getString("Lobby.World"));
/*  87 */         final World defaultWorldInstance = Bukkit.getWorld(getConfig().getString("Command.DefaultDestination.World"));
/*  88 */         (new BukkitRunnable() {
/*     */             public void run() {
/*  90 */               lobbyWorldInstance.setFullTime(defaultWorldInstance.getFullTime());
/*     */             }
/*  92 */           }).runTaskTimer((Plugin)this, 1L, 1200L);
/*     */       }
/*  94 */       else if (getConfig().getBoolean("Lobby.UseDefaultLobbyGamerules")) {
/*  95 */         Bukkit.getWorld(getConfig().getString("Lobby.World")).setGameRule(GameRule.DO_DAYLIGHT_CYCLE, Boolean.valueOf(true));
/*  96 */         Bukkit.getWorld(getConfig().getString("Lobby.World")).setGameRule(GameRule.DO_WEATHER_CYCLE, Boolean.valueOf(true));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 104 */     this.data.saveConfig();
/*     */   }
/*     */   
/*     */   public void teleportToLobby(Player p) {
/* 108 */     Location previousloc = p.getLocation();
/* 109 */     double x = previousloc.getX();
/* 110 */     double y = previousloc.getY();
/* 111 */     double z = previousloc.getZ();
/* 112 */     World world = previousloc.getWorld();
/* 113 */     this.data.getConfig().set("players." + p.getUniqueId().toString() + ".locationBeforeLobby.x", Double.valueOf(x));
/* 114 */     this.data.getConfig().set("players." + p.getUniqueId().toString() + ".locationBeforeLobby.y", Double.valueOf(y));
/* 115 */     this.data.getConfig().set("players." + p.getUniqueId().toString() + ".locationBeforeLobby.z", Double.valueOf(z));
/* 116 */     this.data.getConfig().set("players." + p.getUniqueId().toString() + ".locationBeforeLobby.world", world.getName());
/* 117 */     if (getConfig().getBoolean("Messages.CommandSuccess.Enabled")) p.sendMessage(ChatColor.translateAlternateColorCodes('$', getConfig().getString("Messages.CommandSuccess.Message"))); 
/* 118 */     p.teleport(Bukkit.getWorld("lobby").getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
/*     */   }
/*     */   
/*     */   public void teleportBackFromLobby(final Player p, Boolean command, Boolean endPortal) {
/* 122 */     if (getConfig().getBoolean("Command.ReturnToPreviousLocation") && this.data.getConfig().isSet("players." + p.getUniqueId().toString() + ".locationBeforeLobby.x")) {
/* 123 */       if (!command.booleanValue())
/* 124 */       { if (getConfig().getBoolean("Lobby.ShowMessageIfTeleportedWithPortal") && getConfig().getBoolean("Messages.CommandSuccessReturn.Enabled")) p.sendMessage(ChatColor.translateAlternateColorCodes('$', getConfig().getString("Messages.CommandSuccessReturn.Message")));
/*     */          }
/* 126 */       else if (getConfig().getBoolean("Messages.CommandSuccessReturn.Enabled")) { p.sendMessage(ChatColor.translateAlternateColorCodes('$', getConfig().getString("Messages.CommandSuccessReturn.Message"))); }
/*     */       
/* 128 */       p.teleport(new Location(Bukkit.getWorld((String)this.data.getConfig().get("players." + p.getUniqueId().toString() + ".locationBeforeLobby.world")), ((Double)this.data.getConfig().get("players." + p.getUniqueId().toString() + ".locationBeforeLobby.x")).doubleValue(), ((Double)this.data.getConfig().get("players." + p.getUniqueId().toString() + ".locationBeforeLobby.y")).doubleValue(), ((Double)this.data.getConfig().get("players." + p.getUniqueId().toString() + ".locationBeforeLobby.z")).doubleValue()), PlayerTeleportEvent.TeleportCause.PLUGIN);
/* 129 */       (new BukkitRunnable() {
/*     */           public void run() {
/* 131 */             p.teleport(new Location(Bukkit.getWorld((String)Main.this.data.getConfig().get("players." + p.getUniqueId().toString() + ".locationBeforeLobby.world")), ((Double)Main.this.data.getConfig().get("players." + p.getUniqueId().toString() + ".locationBeforeLobby.x")).doubleValue(), ((Double)Main.this.data.getConfig().get("players." + p.getUniqueId().toString() + ".locationBeforeLobby.y")).doubleValue(), ((Double)Main.this.data.getConfig().get("players." + p.getUniqueId().toString() + ".locationBeforeLobby.z")).doubleValue()), PlayerTeleportEvent.TeleportCause.PLUGIN);
/*     */           }
/* 133 */         }).runTaskLater((Plugin)this, 1L);
/*     */     } else {
/* 135 */       Location loc = null;
/* 136 */       if (!command.booleanValue())
/* 137 */       { if (getConfig().getBoolean("Lobby.ShowMessageIfTeleportedWithPortal") && getConfig().getBoolean("Messages.CommandSuccessReturnNoPreviousLocation.Enabled")) p.sendMessage(ChatColor.translateAlternateColorCodes('$', getConfig().getString("Messages.CommandSuccessReturnNoPreviousLocation.Message")));
/*     */          }
/* 139 */       else if (getConfig().getBoolean("Messages.CommandSuccessReturnNoPreviousLocation.Enabled")) { p.sendMessage(ChatColor.translateAlternateColorCodes('$', getConfig().getString("Messages.CommandSuccessReturnNoPreviousLocation.Message"))); }
/*     */       
/* 141 */       if (getConfig().getBoolean("Command.DefaultDestination.OverrideCoordinates")) {
/* 142 */         loc = new Location(Bukkit.getWorld(getConfig().getString("Command.DefaultDestination.World")), getConfig().getInt("Command.DefaultDestination.Coordinates.X"), getConfig().getInt("Command.DefaultDestination.Coordinates.Y"), getConfig().getInt("Command.DefaultDestination.Coordinates.Z"));
/*     */       } else {
/* 144 */         loc = Bukkit.getWorld(getConfig().getString("Command.DefaultDestination.World")).getSpawnLocation();
/*     */       } 
/* 146 */       final Location finalloc = loc;
/* 147 */       p.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);
/* 148 */       (new BukkitRunnable() {
/*     */           public void run() {
/* 150 */             p.teleport(finalloc, PlayerTeleportEvent.TeleportCause.PLUGIN);
/*     */           }
/* 152 */         }).runTaskLater((Plugin)this, 1L);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
/* 157 */     if (label.equalsIgnoreCase("lobby")) {
/* 158 */       if (sender instanceof Player)
/* 159 */       { Player p = (Player)sender;
/* 160 */         if (getConfig().contains("Command.RequiresPermission") && getConfig().isBoolean("Command.RequiresPermission") && getConfig().getBoolean("Command.RequiresPermission") && 
/* 161 */           !p.hasPermission("lobbyworld.command")) {
/* 162 */           if (getConfig().getBoolean("Messages.CommandPermissionError.Enabled")) sender.sendMessage(ChatColor.translateAlternateColorCodes('$', getConfig().getString("Messages.CommandPermissionError.Message"))); 
/* 163 */           return true;
/*     */         } 
/*     */         
/* 166 */         if (getConfig().getBoolean("Command.BlockWhenInCombat")) {
/* 167 */           int lobbyDamageCooldownTime = getConfig().getInt("Command.CombatSeconds");
/* 168 */           if (this.lobbylastdamagecooldown.containsKey(p)) {
/* 169 */             long secondsLeft = ((Long)this.lobbylastdamagecooldown.get(p)).longValue() / 1000L + lobbyDamageCooldownTime - System.currentTimeMillis() / 1000L;
/* 170 */             if (secondsLeft > 0L) {
/* 171 */               if (getConfig().getBoolean("Messages.CommandCombatError.Enabled")) p.sendMessage(ChatColor.translateAlternateColorCodes('$', getConfig().getString("Messages.CommandCombatError.Message"))); 
/* 172 */               return true;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 177 */         int lobbyCooldownTime = getConfig().getInt("Command.CooldownSeconds");
/* 178 */         if (this.lobbycooldown.containsKey(p)) {
/* 179 */           long secondsLeft = ((Long)this.lobbycooldown.get(p)).longValue() / 1000L + lobbyCooldownTime - System.currentTimeMillis() / 1000L;
/* 180 */           if (secondsLeft > 0L) {
/* 181 */             if (getConfig().getBoolean("Messages.CommandCooldownError.Enabled")) p.sendMessage(ChatColor.translateAlternateColorCodes('$', getConfig().getString("Messages.CommandCooldownError.Message"))); 
/* 182 */             return true;
/*     */           } 
/*     */         } 
/* 185 */         this.lobbycooldown.put(p, Long.valueOf(System.currentTimeMillis()));
/*     */         
/* 187 */         if (p.getWorld() != Bukkit.getWorld(getConfig().getString("Lobby.World"))) {
/* 188 */           teleportToLobby(p);
/*     */         } else {
/* 190 */           teleportBackFromLobby(p, Boolean.valueOf(true), Boolean.valueOf(false));
/*     */         } 
/* 192 */         int cooldownTime = 60;
/* 193 */         if (this.saveCooldown != null) {
/* 194 */           long secondsLeft = this.saveCooldown.longValue() / 1000L + cooldownTime - System.currentTimeMillis() / 1000L;
/* 195 */           if (secondsLeft > 0L) {
/* 196 */             return true;
/*     */           }
/*     */         } 
/* 199 */         this.saveCooldown = Long.valueOf(System.currentTimeMillis());
/* 200 */         this.data.saveConfig(); }
/*     */       
/* 202 */       else if (getConfig().getBoolean("Messages.CommandCombatError.Enabled")) { sender.sendMessage(ChatColor.translateAlternateColorCodes('$', getConfig().getString("Messages.CommandConsoleError.Message"))); }
/*     */     
/*     */     }
/* 205 */     return true;
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onBlockBreak(BlockBreakEvent e) {
/* 210 */     if (e.getBlock().getWorld() == Bukkit.getWorld(getConfig().getString("Lobby.World")) && getConfig().getBoolean("Lobby.BlockModification")) {
/* 211 */       if (e.getPlayer().getGameMode() == GameMode.CREATIVE && e.getPlayer().hasPermission("lobbyworld.modify"))
/*     */         return; 
/* 213 */       e.setCancelled(true);
/*     */       return;
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onBlockPlace(BlockPlaceEvent e) {
/* 220 */     if (e.getBlock().getWorld() == Bukkit.getWorld(getConfig().getString("Lobby.World")) && getConfig().getBoolean("Lobby.BlockModification")) {
/* 221 */       if (e.getPlayer().getGameMode() == GameMode.CREATIVE && e.getPlayer().hasPermission("lobbyworld.modify"))
/*     */         return; 
/* 223 */       e.setCancelled(true);
/*     */       return;
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onInteract(PlayerInteractEvent e) {
/* 230 */     if (e.getPlayer().getWorld() == Bukkit.getWorld(getConfig().getString("Lobby.World")) && getConfig().getBoolean("Lobby.BlockModification")) {
/* 231 */       if (e.getPlayer().getGameMode() == GameMode.CREATIVE && e.getPlayer().hasPermission("lobbyworld.modify"))
/*     */         return; 
/* 233 */       if (e.hasItem()) {
/* 234 */         if (e.getItem().getType() == Material.LEAD) {
/* 235 */           e.setCancelled(true); return;
/*     */         } 
/* 237 */         if (e.getItem().getType() == Material.MAP) {
/* 238 */           e.setCancelled(true); return;
/*     */         } 
/* 240 */         if (e.getItem().getType() == Material.BUCKET) {
/* 241 */           e.setCancelled(true); return;
/*     */         } 
/* 243 */         if (e.getItem().getType() == Material.WATER_BUCKET) {
/* 244 */           e.setCancelled(true); return;
/*     */         } 
/* 246 */         if (e.getItem().getType() == Material.LAVA_BUCKET) {
/* 247 */           e.setCancelled(true); return;
/*     */         } 
/* 249 */         if (e.getItem().getType() == Material.BOW) {
/* 250 */           e.setCancelled(true); return;
/*     */         } 
/* 252 */         if (e.getItem().getType() == Material.TRIDENT) {
/* 253 */           e.setCancelled(true); return;
/*     */         } 
/* 255 */         if (e.getItem().getType() == Material.CROSSBOW) {
/* 256 */           e.setCancelled(true); return;
/*     */         } 
/* 258 */         if (e.getItem().getType() == Material.FISHING_ROD) {
/* 259 */           e.setCancelled(true); return;
/*     */         } 
/* 261 */         if (e.getItem().getType() == Material.WOODEN_HOE) {
/* 262 */           e.setCancelled(true); return;
/*     */         } 
/* 264 */         if (e.getItem().getType() == Material.STONE_HOE) {
/* 265 */           e.setCancelled(true); return;
/*     */         } 
/* 267 */         if (e.getItem().getType() == Material.IRON_HOE) {
/* 268 */           e.setCancelled(true); return;
/*     */         } 
/* 270 */         if (e.getItem().getType() == Material.GOLDEN_HOE) {
/* 271 */           e.setCancelled(true); return;
/*     */         } 
/* 273 */         if (e.getItem().getType() == Material.DIAMOND_HOE) {
/* 274 */           e.setCancelled(true); return;
/*     */         } 
/* 276 */         if (e.getItem().getType() == Material.NETHERITE_HOE) {
/* 277 */           e.setCancelled(true); return;
/*     */         } 
/* 279 */         if (e.getItem().getType() == Material.WOODEN_AXE) {
/* 280 */           e.setCancelled(true); return;
/*     */         } 
/* 282 */         if (e.getItem().getType() == Material.STONE_AXE) {
/* 283 */           e.setCancelled(true); return;
/*     */         } 
/* 285 */         if (e.getItem().getType() == Material.IRON_AXE) {
/* 286 */           e.setCancelled(true); return;
/*     */         } 
/* 288 */         if (e.getItem().getType() == Material.GOLDEN_AXE) {
/* 289 */           e.setCancelled(true); return;
/*     */         } 
/* 291 */         if (e.getItem().getType() == Material.DIAMOND_AXE) {
/* 292 */           e.setCancelled(true); return;
/*     */         } 
/* 294 */         if (e.getItem().getType() == Material.NETHERITE_AXE) {
/* 295 */           e.setCancelled(true); return;
/*     */         } 
/* 297 */         if (e.getItem().getType() == Material.WOODEN_SHOVEL) {
/* 298 */           e.setCancelled(true); return;
/*     */         } 
/* 300 */         if (e.getItem().getType() == Material.STONE_SHOVEL) {
/* 301 */           e.setCancelled(true); return;
/*     */         } 
/* 303 */         if (e.getItem().getType() == Material.IRON_SHOVEL) {
/* 304 */           e.setCancelled(true); return;
/*     */         } 
/* 306 */         if (e.getItem().getType() == Material.GOLDEN_SHOVEL) {
/* 307 */           e.setCancelled(true); return;
/*     */         } 
/* 309 */         if (e.getItem().getType() == Material.DIAMOND_SHOVEL) {
/* 310 */           e.setCancelled(true); return;
/*     */         } 
/* 312 */         if (e.getItem().getType() == Material.NETHERITE_SHOVEL) {
/* 313 */           e.setCancelled(true); return;
/*     */         } 
/* 315 */         if (e.getItem().getType() == Material.FIRE_CHARGE) {
/* 316 */           e.setCancelled(true); return;
/*     */         } 
/* 318 */         if (e.getItem().getType() == Material.FLINT_AND_STEEL) {
/* 319 */           e.setCancelled(true);
/*     */           return;
/*     */         } 
/*     */       } 
/* 323 */       boolean cancelBlock = false;
/* 324 */       for (Object o : getConfig().getList("Lobby.InteractWhitelist")) {
/* 325 */         if (o instanceof String) {
/* 326 */           Material m = Material.getMaterial((String)o);
/* 327 */           if (m instanceof Material && 
/* 328 */             e.getClickedBlock() != null && e.getClickedBlock().getType() == m) {
/* 329 */             cancelBlock = true;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/* 335 */       if (e.getClickedBlock() != null && 
/* 336 */         !e.getClickedBlock().getType().isInteractable()) {
/*     */         return;
/*     */       }
/*     */       
/* 340 */       if (cancelBlock)
/*     */         return; 
/* 342 */       e.setCancelled(true);
/*     */       return;
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onConsume(PlayerItemConsumeEvent e) {
/* 349 */     if (e.getPlayer().getWorld() == Bukkit.getWorld(getConfig().getString("Lobby.World")) && getConfig().getBoolean("Lobby.BlockHungerChange")) {
/* 350 */       if (e.getPlayer().getGameMode() == GameMode.CREATIVE && e.getPlayer().hasPermission("lobbyworld.modify"))
/*     */         return; 
/* 352 */       e.setCancelled(true);
/*     */       return;
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void healthChanged(EntityRegainHealthEvent e) {
/* 359 */     if (e.getEntity() instanceof Player) {
/* 360 */       Player p = (Player)e.getEntity();
/* 361 */       if (p.getWorld() == Bukkit.getWorld(getConfig().getString("Lobby.World")) && getConfig().getBoolean("Lobby.BlockHealing")) {
/* 362 */         if (p.getGameMode() == GameMode.CREATIVE && p.hasPermission("lobbyworld.modify"))
/*     */           return; 
/* 364 */         e.setCancelled(true);
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void hungerChanged(FoodLevelChangeEvent e) {
/* 372 */     if (e.getEntity() instanceof Player) {
/* 373 */       Player p = (Player)e.getEntity();
/* 374 */       if (p.getWorld() == Bukkit.getWorld(getConfig().getString("Lobby.World")) && getConfig().getBoolean("Lobby.BlockHungerChange")) {
/* 375 */         if (p.getGameMode() == GameMode.CREATIVE && p.hasPermission("lobbyworld.modify"))
/*     */           return; 
/* 377 */         e.setCancelled(true);
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onDrop(PlayerDropItemEvent e) {
/* 385 */     Player p = e.getPlayer();
/* 386 */     if (p.getWorld() == Bukkit.getWorld(getConfig().getString("Lobby.World")) && getConfig().getBoolean("Lobby.BlockItemDropping")) {
/* 387 */       if (e.getPlayer().getGameMode() == GameMode.CREATIVE && e.getPlayer().hasPermission("lobbyworld.modify"))
/*     */         return; 
/* 389 */       e.setCancelled(true);
/*     */       return;
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onJoin(PlayerJoinEvent e) {
/* 396 */     Player p = e.getPlayer();
/* 397 */     if (!p.hasPlayedBefore() && 
/* 398 */       getConfig().getBoolean("Lobby.OverrideDefaultSpawnLocation")) p.teleport(Bukkit.getWorld(getConfig().getString("Lobby.World")).getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
/*     */   
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onDamage(EntityDamageEvent e) {
/* 404 */     if (e.getEntity() instanceof Player) {
/* 405 */       if (e.getEntity().getWorld() == Bukkit.getWorld(getConfig().getString("Lobby.World"))) {
/* 406 */         if (getConfig().getBoolean("Lobby.InvulnerablePlayers")) {
/* 407 */           if (e.getCause() == EntityDamageEvent.DamageCause.VOID) {
/* 408 */             e.getEntity().teleport(Bukkit.getWorld("lobby").getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
/* 409 */             e.setCancelled(true);
/*     */             return;
/*     */           } 
/* 412 */           e.setCancelled(true);
/*     */           return;
/*     */         } 
/*     */       } else {
/* 416 */         this.lobbylastdamagecooldown.put((Player)e.getEntity(), Long.valueOf(System.currentTimeMillis()));
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onEntityDamage(EntityDamageByEntityEvent e) {
/* 423 */     if (e.getDamager() instanceof Player && !(e.getEntity() instanceof Player)) {
/* 424 */       if (e.getDamager().getWorld() == Bukkit.getWorld(getConfig().getString("Lobby.World"))) {
/* 425 */         if (getConfig().getBoolean("Lobby.BlockModification")) {
/* 426 */           if (((Player)e.getDamager()).getGameMode() == GameMode.CREATIVE && ((Player)e.getDamager()).hasPermission("lobbyworld.modify"))
/*     */             return; 
/* 428 */           e.setCancelled(true);
/*     */           return;
/*     */         } 
/*     */       } else {
/* 432 */         this.lobbylastdamagecooldown.put((Player)e.getDamager(), Long.valueOf(System.currentTimeMillis()));
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onDeath(PlayerDeathEvent e) {
/* 439 */     Player p = e.getEntity();
/* 440 */     this.data.getConfig().set("players." + p.getUniqueId().toString() + ".locationBeforeLobby.x", null);
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void playerTeleportEvent(PlayerTeleportEvent e) {
/* 445 */     Player p = e.getPlayer();
/* 446 */     if (p.getWorld() == Bukkit.getWorld(getConfig().getString("Lobby.World"))) {
/* 447 */       if (e.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
/* 448 */         if (getConfig().getBoolean("Lobby.NetherPortals.OverrideDestination")) {
/* 449 */           teleportBackFromLobby(p, Boolean.valueOf(false), Boolean.valueOf(false));
/* 450 */           e.setCancelled(true);
/*     */           return;
/*     */         } 
/* 453 */       } else if (e.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL && 
/* 454 */         getConfig().getBoolean("Lobby.EndPortals.OverrideDestination")) {
/* 455 */         teleportBackFromLobby(p, Boolean.valueOf(false), Boolean.valueOf(true));
/* 456 */         e.setCancelled(true);
/*     */         return;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void onRespawn(PlayerRespawnEvent e) {
/* 465 */     Player p = e.getPlayer();
/* 466 */     if (p.getBedSpawnLocation() == null && 
/* 467 */       getConfig().getBoolean("Lobby.OverrideDefaultSpawnLocation")) e.setRespawnLocation(Bukkit.getWorld(getConfig().getString("Lobby.World")).getSpawnLocation());
/*     */     
/* 469 */     this.lobbylastdamagecooldown.remove(p);
/*     */   }
/*     */ }


/* Location:              C:\Users\Nathan\Desktop\OnyxionDEV\server\plugins\amogus.jar!\me\ArttuPoika05\lobbyworld\Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */