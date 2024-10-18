package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Runnables;

import java.awt.*;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import gg.vape.Medusa;
import gg.vape.helpers.GuiClientTextField;
import gg.vape.helpers.HoverUtility;
import gg.vape.helpers.animation.Animation;
import gg.vape.helpers.animation.Direction;
import gg.vape.helpers.animation.impl.EaseInOutQuad;
import gg.vape.helpers.animbackground;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.settings.GameSettings;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import net.optifine.reflect.Reflector;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GLContext;
import gg.vape.helpers.font.Fonts;
import gg.vape.helpers.math.MathHelper;
import gg.vape.helpers.render.RenderUtil;
import gg.vape.helpers.render.RoundedUtil;

public class GuiMainMenu extends GuiScreen
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Random RANDOM = new Random();

    /**
     * A random number between 0.0 and 1.0, used to determine if the title screen says <a
     * href="https://minecraft.gamepedia.com/Menu_screen#Minceraft">Minceraft</a> instead of Minecraft. Set during
     * construction; if the value is less than .0001, then Minceraft is displayed.
     */
    private final float minceraftRoll;

    /** The splash message. */
    private String splashText;
    private GuiButton buttonResetDemo;

    /** Timer used to rotate the panorama, increases every tick. */
    private float panoramaTimer;

    /**
     * Texture allocated for the current viewport of the main menu's panorama background.
     */
    private DynamicTexture viewportTexture;

    /**
     * The Object object utilized as a thread lock when performing non thread-safe operations
     */
    private final Object threadLock = new Object();
    public static final String MORE_INFO_TEXT = "Please click " + TextFormatting.UNDERLINE + "here" + TextFormatting.RESET + " for more information.";

    /** Width of openGLWarning2 */
    private int openGLWarning2Width;

    /** Width of openGLWarning1 */
    private int openGLWarning1Width;

    /** Left x coordinate of the OpenGL warning */
    private int openGLWarningX1;

    /** Top y coordinate of the OpenGL warning */
    private int openGLWarningY1;

    /** Right x coordinate of the OpenGL warning */
    private int openGLWarningX2;

    /** Bottom y coordinate of the OpenGL warning */
    private int openGLWarningY2;

    /** OpenGL graphics card warning. */
    private String openGLWarning1;

    /** OpenGL graphics card warning. */
    private String openGLWarning2;

    /** Link to the Mojang Support about minimum requirements */
    private String openGLWarningLink;
    private static final ResourceLocation SPLASH_TEXTS = new ResourceLocation("texts/splashes.txt");
    private static final ResourceLocation MINECRAFT_TITLE_TEXTURES = new ResourceLocation("textures/gui/title/minecraft.png");
    private static final ResourceLocation field_194400_H = new ResourceLocation("textures/gui/title/edition.png");

    /** An array of all the paths to the panorama pictures. */
    private static final ResourceLocation[] TITLE_PANORAMA_PATHS = new ResourceLocation[] {new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png")};
    private ResourceLocation backgroundTexture;

    /** Minecraft Realms button. */
    private GuiButton realmsButton;

    /** Has the check for a realms notification screen been performed? */
    private boolean hasCheckedForRealmsNotification;

    /**
     * A screen generated by realms for notifications; drawn in adition to the main menu (buttons and such from both are
     * drawn at the same time). May be null.
     */
    private GuiScreen realmsNotification;
    private int widthCopyright;
    private int widthCopyrightRest;
    private GuiButton modButton;
    private GuiScreen modUpdateNotification;

    public ArrayList<Particle> p = new ArrayList<>();
    private animbackground backgroundShader;
    private long initTime = System.currentTimeMillis();

    public GuiMainMenu() {
        this.initTime = System.currentTimeMillis();
        try {
            this.backgroundShader = new animbackground("/assets/minecraft/vapegg/shaders/background.fsh");
        }
        catch (IOException var2) {
            throw new IllegalStateException("Failed to load backgound shader", var2);
        }
        Document a = null;
        for (int i = 0; i < 100; i++) {
            Particle p1 = new Particle();

            p.add(p1);

        }
        this.openGLWarning2 = MORE_INFO_TEXT;
        this.splashText = "missingno";
        IResource iresource = null;

        try
        {
            List<String> list = Lists.<String>newArrayList();
            iresource = Minecraft.getMinecraft().getResourceManager().getResource(SPLASH_TEXTS);
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(iresource.getInputStream(), StandardCharsets.UTF_8));
            String s;

            while ((s = bufferedreader.readLine()) != null)
            {
                s = s.trim();

                if (!s.isEmpty())
                {
                    list.add(s);
                }
            }

            if (!list.isEmpty())
            {
                while (true)
                {
                    this.splashText = list.get(RANDOM.nextInt(list.size()));

                    if (this.splashText.hashCode() != 125780783)
                    {
                        break;
                    }
                }
            }
        }
        catch (IOException var8)
        {
            ;
        }
        finally
        {
            IOUtils.closeQuietly((Closeable)iresource);
        }

        this.minceraftRoll = RANDOM.nextFloat();
        this.openGLWarning1 = "";

        if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.areShadersSupported())
        {
            this.openGLWarning1 = I18n.format("title.oldgl1");
            this.openGLWarning2 = I18n.format("title.oldgl2");
            this.openGLWarningLink = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
        }
    }

    /**
     * Is there currently a realms notification screen, and are realms notifications enabled?
     */
    private boolean areRealmsNotificationsEnabled()
    {
        return Minecraft.getMinecraft().gameSettings.getOptionOrdinalValue(GameSettings.Options.REALMS_NOTIFICATIONS) && this.realmsNotification != null;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        this.inputField.updateCursorCounter();
        if (this.areRealmsNotificationsEnabled())
        {
            this.realmsNotification.updateScreen();
        }
    }
    GuiClientTextField inputField;
    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
//    protected void keyTyped(char typedChar, int keyCode) throws IOException
//    {
//    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui()
    {

        final ScaledResolution sr = new ScaledResolution(this.mc);
        (this.inputField = new GuiClientTextField(1, mc.fontRenderer, (sr.getScaledWidth() / 2 + 85),(sr.getScaledHeight() / 4 + 48 + 72 + 12), 130, 20)).setText("");
        this.viewportTexture = new DynamicTexture(256, 256);
        this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
        this.widthCopyright = this.fontRenderer.getStringWidth("Copyright Mojang AB. Do not distribute!");
        this.widthCopyrightRest = this.width - this.widthCopyright - 2;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        if (calendar.get(2) + 1 == 12 && calendar.get(5) == 24)
        {
            this.splashText = "Merry X-mas!";
        }
        else if (calendar.get(2) + 1 == 1 && calendar.get(5) == 1)
        {
            this.splashText = "Happy new year!";
        }
        else if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31)
        {
            this.splashText = "OOoooOOOoooo! Spooky!";
        }

        int i = 24;
        int j = this.height / 4 + 48;

        if (this.mc.isDemo())
        {
            this.addDemoButtons(j, 24);
        }
        else
        {
            this.addSingleplayerMultiplayerButtons(j, 24);
        }

//        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, j + 72 + 12, 98, 20, I18n.format("menu.options")));
//        this.buttonList.add(new GuiButton(4, this.width / 2 + 2, j + 72 + 12, 98, 20, I18n.format("menu.quit")));

        synchronized (this.threadLock)
        {
            this.openGLWarning1Width = this.fontRenderer.getStringWidth(this.openGLWarning1);
            this.openGLWarning2Width = this.fontRenderer.getStringWidth(this.openGLWarning2);
            int k = Math.max(this.openGLWarning1Width, this.openGLWarning2Width);
            this.openGLWarningX1 = (this.width - k) / 2;
            this.openGLWarningY1 = (this.buttonList.get(0)).y - 24;
            this.openGLWarningX2 = this.openGLWarningX1 + k;
            this.openGLWarningY2 = this.openGLWarningY1 + 24;
        }

        this.mc.setConnectedToRealms(false);


        if (Reflector.NotificationModUpdateScreen_init.exists())
        {
            this.modUpdateNotification = (GuiScreen)Reflector.call(Reflector.NotificationModUpdateScreen_init, this, this.modButton);
        }
    }

    /**
     * Adds Singleplayer and Multiplayer buttons on Vape Menu for players who have bought the game.
     */
    private void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_)
    {
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, p_73969_1_ - 30, 100, 20, I18n.format("menu.singleplayer")));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 1 - 30, 100, 20, I18n.format("menu.multiplayer")));
        this.buttonList.add(new GuiButton(1000, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 2 - 30, 100, 20, I18n.format("Alt Manager")));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 3 - 30, 100, 20, I18n.format("menu.options")));
        this.buttonList.add(new GuiButton(4, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 4 - 30, 100, 20, I18n.format("menu.quit")));
//        this.buttonList.add(new GuiButton(1001, this.width / 2 - 100 - 45, p_73969_1_ + p_73969_2_ * 5 - 30, I18n.format("Shaders")));

    }

    /**
     * Adds Demo buttons on Vape Menu for players who are playing Demo.
     */
    private void addDemoButtons(int p_73972_1_, int p_73972_2_)
    {
        this.buttonList.add(new GuiButton(11, this.width / 2 - 100, p_73972_1_, I18n.format("menu.playdemo")));
        this.buttonResetDemo = this.addButton(new GuiButton(12, this.width / 2 - 100, p_73972_1_ + p_73972_2_ * 1, I18n.format("menu.resetdemo")));
        ISaveFormat isaveformat = this.mc.getSaveLoader();
        WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");

        if (worldinfo == null)
        {
            this.buttonResetDemo.enabled = false;
        }
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }
        if (button.id == 1) {
            changeName(this.inputField.getText());
        }
        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiWorldSelection(this));
        }
        if (button.id == 1000) {
            if(!altM) {
                altM = true;
            } else if(altM){
                altM = false;
            }
        }
        if (button.id == 2) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }

        if (button.id == 4) {
            this.mc.shutdown();
        }

        if (button.id == 6 && Reflector.GuiModList_Constructor.exists()) {
            this.mc.displayGuiScreen((GuiScreen) Reflector.newInstance(Reflector.GuiModList_Constructor, this));
        }

    }

    @Override
    protected void keyTyped( char typedChar,  int keyCode) throws IOException {
        switch (keyCode) {
            case 1: {
                this.mc.displayGuiScreen(new GuiMainMenu());
                break;
            }
            default: {
                this.inputField.textboxKeyTyped(typedChar, keyCode);
                break;
            }
        }
    }
    public static void setSession(final Session s) {
        final Class<? extends Minecraft> mc = Minecraft.getMinecraft().getClass();
        try {
            Field session = null;
            for (final Field f : mc.getDeclaredFields()) {
                if (f.getType().isInstance(s)) {
                    session = f;
                }
            }
            if (session == null) {
                throw new IllegalStateException("Session Null");
            }
            session.setAccessible(true);
            session.set(Minecraft.getMinecraft(), s);
            session.setAccessible(false);
        }
        catch (Exception var6) {
            var6.printStackTrace();
        }
    }
    public static void changeName( String name) {
        final YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        final YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
        auth.logOut();
        final Session session = new Session(name, name, "0", "legacy");
        try {
            setSession(session);
        }
        catch (Exception var5) {
            var5.printStackTrace();
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {

        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd   HH:mm:ss");
        Date date = new Date();
        ScaledResolution sr = new ScaledResolution(mc);
        RenderUtil.drawRectWH(0, 0, width, height, new Color(36, 36, 36).getRGB());
        GlStateManager.disableCull();
        this.backgroundShader.useShader(sr.getScaledWidth() + 800, sr.getScaledHeight(), mouseX, mouseY, (float) (System.currentTimeMillis() - this.initTime) /
                1000.0f);
        GL11.glBegin(7);
        GL11.glVertex2f(-1.0f, -1.0f);
        GL11.glVertex2f(-1.0f, 1.0f);
        GL11.glVertex2f(1.0f, 1.0f);
        GL11.glVertex2f(1.0f, -1.0f);
        GL11.glEnd();
        GL20.glUseProgram(0);

        GlStateManager.disableCull();
        animation2.setDirection((altM ? Direction.FORWARDS : Direction.BACKWARDS));

        RoundedUtil.drawRound(this.width / 2f - 105, this.height / 4f + 10, 230, 140, 6, new Color(10, 10, 10, 100));
        RenderUtil.drawTexture(new ResourceLocation("vapegg/vape.png"), this.width / 2f - 40 + 45, this.height / 4f + 20, 75, 20, new Color(255, 255, 255, 255));
        Fonts.BOLD18.drawCenteredString("PRIVATE", this.width / 2f - 14 + 45, this.height / 4f + 42, -1);
        Fonts.BOLD18.drawString("UID: " + Medusa.uid, this.width / 2f - 40 + 45, this.height / 4f + 70, -1);
        Fonts.BOLD18.drawString("Title: " + Medusa.title, this.width / 2f - 40 + 45, this.height / 4f + 80, -1);
        Fonts.BOLD18.drawString("Version: " + Medusa.version, this.width / 2f - 40 + 45, this.height / 4f + 90, -1);
        Fonts.BOLD18.drawString("Till: " + Medusa.till, this.width / 2f - 40 + 45, this.height / 4f + 100, -1);
        Fonts.BOLD18.drawString("Username: " + Medusa.username, this.width / 2f - 40 + 45, this.height / 4f + 110, new Color(255, 255, 255).getRGB());
        Fonts.BOLD18.drawString("\u0422\u0432\u043e\u0439 \u043d\u0438\u043a: " + this.mc.getSession().getUsername(), this.width / 2f - 40 + 45, this.height / 4f + 120, Color.WHITE.getRGB());
        Fonts.BOLD18.drawString("Welcome: " + Medusa.username, 2, sr.getScaledHeight() - 9, new Color(255, 255, 255).getRGB());
        Fonts.BOLD18.drawString(dateFormat.format(date), this.width / 2f - 105 - 40 + 45, this.height / 4f + 140, -1);

//        RenderUtil.drawTexture(new ResourceLocation("icons/bb/altmanager/mojang.png"), 12 - 200 + (animation2.getOutput() * 200), 95, 30, 30, new Color(255, 255, 255, 255));

        RoundedUtil.drawRound((float) (15 - 200 + (animation2.getOutput() * 200)), 5, 140, 90, 2.0f, new Color(10, 10, 10, 100));
        Fonts.BOLD18.drawString("Alt Manager", 20 - 200 + (animation2.getOutput() * 200), 85, -1);

        RoundedUtil.drawRound((float) (20 - 200 + (animation2.getOutput() * 200)), 35, 130, 20.0f, 2.0f, new Color(0, 0, 0, 100));
        Fonts.BOLD18.drawString("\u0412\u043e\u0439\u0442\u0438", 65 + 5 - 200 + (animation2.getOutput() * 200), 42, -1);

        RoundedUtil.drawRound((float) (20 - 200 + (animation2.getOutput() * 200)), 60, 130, 20.0f, 2.0f, new Color(0, 0, 0, 100));
        Fonts.BOLD18.drawString("Random", 62 + 5 - 200 + (animation2.getOutput() * 200),67, -1);

        this.inputField.drawTextBox();



//        p.forEach(DamageParticles::draw);
        super.drawScreen(mouseX, mouseY, partialTicks);

    }
    public Animation animation2 = new EaseInOutQuad(250, 1);
    public static boolean altM = false;

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        animation2.setDirection((altM ? Direction.FORWARDS : Direction.BACKWARDS));
        super.mouseClicked(mouseX, mouseY, mouseButton);
        final ScaledResolution sr = new ScaledResolution(this.mc);

            if (HoverUtility.isHovered(mouseX, mouseY, 20 - 200 + (animation2.getOutput() * 200), 35, 130, 20.0f)) {
                changeName(this.inputField.getText());
            }
            if (HoverUtility.isHovered(mouseX, mouseY, 20 - 200 + (animation2.getOutput() * 200), 60, 130, 20.0f)) {
                changeName("Vape" + RandomStringUtils.randomAlphabetic(6));
            }
            this.inputField.mouseClicked(mouseX, mouseY, mouseButton);

        synchronized (this.threadLock)
        {
            if (!this.openGLWarning1.isEmpty() && !StringUtils.isNullOrEmpty(this.openGLWarningLink) && mouseX >= this.openGLWarningX1 && mouseX <= this.openGLWarningX2 && mouseY >= this.openGLWarningY1 && mouseY <= this.openGLWarningY2)
            {
                GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink(this, this.openGLWarningLink, 13, true);
                guiconfirmopenlink.disableSecurityWarning();
                this.mc.displayGuiScreen(guiconfirmopenlink);
            }
        }

        if (this.areRealmsNotificationsEnabled())
        {
            this.realmsNotification.mouseClicked(mouseX, mouseY, mouseButton);
        }

        if (mouseX > this.widthCopyrightRest && mouseX < this.widthCopyrightRest + this.widthCopyright && mouseY > this.height - 10 && mouseY < this.height)
        {
            this.mc.displayGuiScreen(new GuiWinGame(false, Runnables.doNothing()));
        }
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        if (this.realmsNotification != null)
        {
            this.realmsNotification.onGuiClosed();
        }
    }

    public class Particle {
        public float x, y, rX, rY;
        public double motionX, motionY;

        public Particle() {
            x = MathHelper.getRandomNumberBetween(0, Minecraft.getMinecraft().displayWidth);
            y = MathHelper.getRandomNumberBetween(0, Minecraft.getMinecraft().displayHeight);
            motionY = MathHelper.getRandomNumberBetween(-2, 2);
            motionX = MathHelper.getRandomNumberBetween(-2, 2);
        }

        public Particle(int x, int y) {
            this.x = x;
            this.y = y;
            motionY = MathHelper.getRandomNumberBetween(-2, 2);
            motionX = MathHelper.getRandomNumberBetween(-2, 2);
        }

        public void draw() {
            if (x < 0) {
                motionX = -motionX;
            }

            if (x > new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth()) {
                motionX = -motionX;
            }

            if (y < 0) {
                motionY = -motionY;
            }

            if (y > new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight()) {
                motionY = -motionY;
            }

                for (Particle p2 : p) {
                    if (p2 != this) {
                        if (p2.x > x - 1 && p2.x < x + 1 && p2.y > y - 1 && p2.y < y + 1) {
                            motionX = -motionX;
                            motionY = -motionY;
                            p2.motionX = -p2.motionX;
                            p2.motionY = -p2.motionY;

                        }
                    }
                }

            x += motionX * 0.1;
            y += motionY * 0.1;

            rX = MathHelper.interpolate(rX, x, 0.1f);
            rY = MathHelper.interpolate(rY, y, 0.1);

            RenderUtil.drawRectWH(rX, rY, 1, 1, new Color(255, 255, 255, 50).getRGB());
        }

    }
}
