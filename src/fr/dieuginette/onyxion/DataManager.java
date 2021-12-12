package fr.dieuginette.onyxion;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import java.util.logging.Level;
/*    */ import org.bukkit.configuration.Configuration;
/*    */ import org.bukkit.configuration.file.FileConfiguration;
/*    */ import org.bukkit.configuration.file.YamlConfiguration;
/*    */ 
/*    */ public class DataManager
/*    */ {
/*    */   private Main plugin;
/* 15 */   private FileConfiguration dataConfig = null;
/* 16 */   private File configFile = null;
/*    */   
/*    */   public DataManager(Main plugin) {
/* 19 */     this.plugin = plugin;
/*    */     
/* 21 */     saveDefaultConfig();
/*    */   }
/*    */   
/*    */   public void reloadConfig() {
/* 25 */     if (this.configFile == null) {
/* 26 */       this.configFile = new File(this.plugin.getDataFolder(), "data.yml");
/*    */     }
/* 28 */     this.dataConfig = (FileConfiguration)YamlConfiguration.loadConfiguration(this.configFile);
/*    */     
/* 30 */     InputStream defaultStream = this.plugin.getResource("data.yml");
/* 31 */     if (defaultStream != null) {
/* 32 */       YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
/* 33 */       this.dataConfig.setDefaults((Configuration)defaultConfig);
/*    */     } 
/*    */   }
/*    */   
/*    */   public FileConfiguration getConfig() {
/* 38 */     if (this.dataConfig == null)
/* 39 */       reloadConfig(); 
/* 40 */     return this.dataConfig;
/*    */   }
/*    */   
/*    */   public void saveConfig() {
/* 44 */     if (this.dataConfig == null || this.configFile == null) {
/*    */       return;
/*    */     }
/*    */     try {
/* 48 */       getConfig().save(this.configFile);
/* 49 */     } catch (IOException e) {
/* 50 */       this.plugin.getLogger().log(Level.SEVERE, "Impossible de sauvegarder la configuration dans " + this.configFile, e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void saveDefaultConfig() {
/* 55 */     if (this.configFile == null) {
/* 56 */       this.configFile = new File(this.plugin.getDataFolder(), "data.yml");
/*    */     }
/* 58 */     if (!this.configFile.exists())
/* 59 */       this.plugin.saveResource("data.yml", false); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Nathan\Desktop\OnyxionDEV\server\plugins\amogus.jar!\me\ArttuPoika05\lobbyworld\DataManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */