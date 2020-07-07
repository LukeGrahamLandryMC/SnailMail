package dev.itsmeow.snailmail.client;

import dev.itsmeow.snailmail.SnailMail;
import dev.itsmeow.snailmail.item.EnvelopeItem.EnvelopeContainer;
import dev.itsmeow.snailmail.network.SetEnvelopeNamePacket;
import dev.itsmeow.snailmail.util.RandomUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class EnvelopeScreen extends ContainerScreen<EnvelopeContainer> {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation("snailmail:textures/gui/envelope_open.png");
    private TextFieldWidget toField;
    private TextFieldWidget fromField;

    public EnvelopeScreen(EnvelopeContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.xSize = 176;
        this.ySize = 178;
    }

    protected void init() {
        super.init();
        this.minecraft.keyboardListener.enableRepeatEvents(true);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.toField = new TextFieldWidget(this.font, i + 92, j + 10, 58, 10, I18n.format("container.snailmail.envelope.textfield.to")) {

            @Override
            public boolean charTyped(char c, int p_charTyped_2_) {
                if(!this.canWrite()) {
                    return false;
                } else if(RandomUtil.isAllowedCharacter(c)) {
                    this.writeText(Character.toString(c));

                    return true;
                } else {
                    return false;
                }
            }

        };
        this.toField.setText(this.container.clientStartToName);
        this.toField.setCanLoseFocus(true);
        this.toField.setTextColor(0xFFFFFF);
        this.toField.setDisabledTextColour(0xFFFFFF);
        this.toField.setEnableBackgroundDrawing(false);
        this.toField.setMaxStringLength(35);
        this.toField.setResponder(newText -> {
            SnailMail.HANDLER.sendToServer(new SetEnvelopeNamePacket(SetEnvelopeNamePacket.Type.TO, newText));
        });
        this.children.add(this.toField);
        
        this.fromField = new TextFieldWidget(this.font, i + 111, j + 84, 58, 10, I18n.format("container.snailmail.envelope.textfield.from")) {

            @Override
            public boolean charTyped(char c, int p_charTyped_2_) {
                if(!this.canWrite()) {
                    return false;
                } else if(RandomUtil.isAllowedCharacter(c)) {
                    this.writeText(Character.toString(c));

                    return true;
                } else {
                    return false;
                }
            }

        };
        this.fromField.setText(this.container.clientStartFromName);
        this.fromField.setCanLoseFocus(true);
        this.fromField.setTextColor(0xFFFFFF);
        this.fromField.setDisabledTextColour(0xFFFFFF);
        this.fromField.setEnableBackgroundDrawing(false);
        this.fromField.setMaxStringLength(35);
        this.fromField.setResponder(newText -> {
            SnailMail.HANDLER.sendToServer(new SetEnvelopeNamePacket(SetEnvelopeNamePacket.Type.FROM, newText));
        });
        this.children.add(this.fromField);
    }

    @Override
    public void resize(Minecraft mc, int x, int y) {
        String s = this.toField.getText();
        String s2  = this.fromField.getText();
        this.init(mc, x, y);
        this.toField.setText(s);
        this.fromField.setText(s2);
    }

    @Override
    public void removed() {
        super.removed();
        this.minecraft.keyboardListener.enableRepeatEvents(false);
    }

    @Override
    public boolean keyPressed(int key, int a, int b) {
        if(key == 256) {
            this.minecraft.player.closeScreen();
        }
        if(toField.isFocused()) {
            if(!this.toField.keyPressed(key, a, b) && !this.toField.canWrite()) {
                return super.keyPressed(key, a, b);
            } else {
                return true;
            }
        } else if(fromField.isFocused()) {
            if(!this.fromField.keyPressed(key, a, b) && !this.fromField.canWrite()) {
                return super.keyPressed(key, a, b);
            } else {
                return true;
            }
        }
        return super.keyPressed(key, a, b);
    }

    @Override
    public void render(int x, int y, float partialTicks) {
        this.renderBackground();
        super.render(x, y, partialTicks);
        this.toField.render(x, y, partialTicks);
        this.fromField.render(x, y, partialTicks);
        this.renderHoveredToolTip(x, y);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        int xStart = (this.width - this.xSize) / 2;
        int yStart = (this.height - this.ySize) / 2;
        this.minecraft.getTextureManager().bindTexture(GUI_TEXTURE);
        this.blit(xStart, yStart, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.font.drawString(this.title.getFormattedText(), 8, 11, 0x404040);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8, 84, 0x404040);
    }
}