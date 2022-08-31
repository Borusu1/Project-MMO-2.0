package harmonised.pmmo.client.gui.configure;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;

import harmonised.pmmo.util.CodecSpec;
import harmonised.pmmo.util.CodecSpec.GroupSpec;
import harmonised.pmmo.util.CodecSpec.ListSpec;
import harmonised.pmmo.util.CodecSpec.MapSpec;
import harmonised.pmmo.util.CodecSpec.NestedMapSpec;
import harmonised.pmmo.util.CodecSpec.PrimativeSpec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.client.gui.widget.ScrollPanel;

public class ConfigScrollWidget extends ScrollPanel{
	private final ConfigEditScreen parent;
	private final CodecSpec spec;
	private CompoundTag existingData;
	private final List<SpecEntry> content = new ArrayList<>();

	public ConfigScrollWidget(ConfigEditScreen parent, int width, int height, int top, int left, CodecSpec spec, CompoundTag existingData) {
		super(Minecraft.getInstance(), width, height, top, left, 4);
		this.parent = parent;
		this.spec = spec;
		this.existingData = existingData;
		System.out.println(existingData.toString()); //TODO remove
		System.out.println(spec.toString()); //TODO remove
		buildContent();
	}
	
	public void setData(CompoundTag inData) {
		existingData = inData;
		buildContent();
	}
	
	private void buildContent() {
		if (spec instanceof CodecSpec.PrimativeSpec) {
			buildPrimativeSpec(0, (PrimativeSpec) spec, existingData.getString(spec.getField()));
		}
		else if (spec instanceof CodecSpec.ListSpec) {
			buildListSpec(0, (ListSpec) spec, existingData.getList(spec.getField(), ((CodecSpec.ListSpec)spec).getTagType()));
		}
		else if (spec instanceof CodecSpec.MapSpec) {
			buildMapSpec(0, (MapSpec) spec, existingData.getCompound(spec.getField()));		
		}
		else if (spec instanceof CodecSpec.NestedMapSpec) {
			buildNestedMapSpec(0, (NestedMapSpec) spec, existingData.getCompound(spec.getField()));
		}
		else {
			buildGroupSpec(0, (GroupSpec) spec, existingData);
		}
	}
	
	private void buildPrimativeSpec(int currentNest, CodecSpec.PrimativeSpec inSpec, String value) {
		content.add(new SpecEntry(currentNest, inSpec.getField(), value));
	}
	
	private void buildListSpec(int currentNest, CodecSpec.ListSpec inSpec, ListTag listSource) {
		content.add(new SpecEntry(currentNest, inSpec.getField(), ""));
		listSource.iterator().forEachRemaining(tag -> {
			content.add(new SpecEntry(currentNest + 1, tag.getAsString()));
		});
	}
	
	private void buildMapSpec(int currentNest, CodecSpec.MapSpec inSpec, CompoundTag mapSource) {
		content.add(new SpecEntry(currentNest, inSpec.getField(), ""));
		for (String key : mapSource.getAllKeys()) {
			content.add(new SpecEntry(currentNest + 1, key, mapSource.get(key).getAsString()));
		}
	}
	
	private void buildNestedMapSpec(int currentNest, CodecSpec.NestedMapSpec inSpec, CompoundTag mapSource) {
		content.add(new SpecEntry(currentNest, inSpec.fieldName(), ""));
		buildMapSpec(currentNest + 1, (MapSpec) ((NestedMapSpec)inSpec).valueTypes(), mapSource.getCompound(inSpec.fieldName()));
	}
	
	private void buildGroupSpec(int currentNest, CodecSpec.GroupSpec inSpec, CompoundTag rootTag) {
		for (CodecSpec subSpec : inSpec.getMembers()) {
			if (subSpec instanceof CodecSpec.PrimativeSpec) {
				buildPrimativeSpec(currentNest + 1, (PrimativeSpec) subSpec, rootTag.getString(subSpec.getField()));
			}
			else if (subSpec instanceof CodecSpec.ListSpec) {
				buildListSpec(currentNest +1, (ListSpec) subSpec, rootTag.getList(subSpec.getField(), ((CodecSpec.ListSpec)subSpec).getTagType()));
			}
			else if (subSpec instanceof CodecSpec.MapSpec) {
				buildMapSpec(currentNest +1, (MapSpec) subSpec, rootTag.getCompound(subSpec.getField()));		
			}
			else if (subSpec instanceof CodecSpec.NestedMapSpec) {
				buildNestedMapSpec(currentNest +1, (NestedMapSpec) subSpec, rootTag.getCompound(subSpec.getField()));
			}
			else {
				content.add(new SpecEntry(currentNest, subSpec.getField(), ""));
				buildGroupSpec(currentNest + 1, (GroupSpec) subSpec, rootTag.getCompound(subSpec.getField()));
			}
		}
	}
	

	@Override
	public NarrationPriority narrationPriority() {return NarrationPriority.NONE;}

	@Override
	public void updateNarration(NarrationElementOutput p_169152_) {}

	@Override
	protected int getContentHeight() {return content.size() * 12;}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
		return super.mouseScrolled(mouseX, mouseY, scroll);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int partialTicks) {
		return super.mouseClicked(mouseX, mouseY, partialTicks);
	}

	@SuppressWarnings("resource")
	@Override
	protected void drawPanel(PoseStack poseStack, int entryRight, int relativeY, Tesselator tess, int mouseX, int mouseY) {
		for (int i = 0; i < content.size(); i++) {
			SpecEntry entry = content.get(i);
			int y = (int)(relativeY + (i*12) - scrollDistance);
//			if (isMouseOver(mouseX, mouseY))
//				GuiComponent.fill(poseStack, this.left, (int) y, this.left+this.width, y+12, 0xAAAAAA);
			GuiComponent.drawString(poseStack, Minecraft.getInstance().font, entry.getText(), this.left+ 1 + entry.getOffset(), y, i % 2 == 0 ? 0xFFFFFF : 0xAAAAAA);
		}		
	}
	
	private class SpecEntry {
		private int offset;
		public String key, value;
		private static final String IGNORE = "IGNORE";
		
		public SpecEntry(int offset, String key, String value) {
			this.offset = offset;
			this.key = key;
			this.value = value == null ? "" : value;
		}
		public SpecEntry(int offset, String key) {
			this(offset, key, IGNORE);
		}
		
		public int getOffset() {return offset * 10;}
		
		public MutableComponent getText() {
			return Component.literal(key + (value.equals(IGNORE) ? "" : " = "+value));
		}
	}
}
