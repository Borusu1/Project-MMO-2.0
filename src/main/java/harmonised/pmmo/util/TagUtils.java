package harmonised.pmmo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.resources.ResourceLocation;

public class TagUtils {

	public static CompoundTag mergeTags(CompoundTag tag1, CompoundTag tag2) {
		CompoundTag output = new CompoundTag();
		List<String> allKeys = new ArrayList<>(); 
		tag1.getAllKeys().forEach(s -> allKeys.add(s));
		for (String key : tag2.getAllKeys()) {
			if (!allKeys.contains(key) && key != null)
				allKeys.add(key);
		}
		for (String key : allKeys) {
			if (tag1.contains(key) && tag2.contains(key)) {
				if (tag1.get(key) instanceof NumericTag) {
					if (tag1.get(key) instanceof DoubleTag)
						output.putDouble(key, tag1.getDouble(key) + tag2.getDouble(key));
					else if (tag1.get(key) instanceof FloatTag)
						output.putFloat(key, tag1.getFloat(key) + tag2.getFloat(key));
					else if (tag1.get(key) instanceof IntTag)
						output.putInt(key, tag1.getInt(key) + tag2.getInt(key));
					else if (tag1.get(key) instanceof LongTag)
						output.putLong(key, tag1.getLong(key) + tag2.getLong(key));
					else if (tag1.get(key) instanceof ShortTag) 
						output.putShort(key, (short)(tag1.getShort(key) + tag2.getShort(key)));
					else
						output.put(key, tag1.get(key));
				}
				else
					output.put(key, tag1.get(key));
			}
			else if (tag1.contains(key) && !tag2.contains(key))
				output.put(key, tag1.get(key));
			else if (!tag1.contains(key) && tag2.contains(key))
				output.put(key, tag2.get(key));				
		}
		return output;
	}
	
	public static CompoundTag intMapToCompound(Map<String, Integer> inMap) {
		CompoundTag nbt = new CompoundTag();
		inMap.forEach((key, value) -> nbt.putInt(key, value));
		return nbt;
	}
	
	public static CompoundTag dblMapToCompound(Map<String, Double> inMap) {
		CompoundTag nbt = new CompoundTag();
		inMap.forEach((key, value) -> nbt.putDouble(key, value));
		return nbt;
	}
	
	public static CompoundTag longMapToCompound(Map<String, Long> inMap) {
		CompoundTag nbt = new CompoundTag();
		inMap.forEach((key, value) -> nbt.putLong(key, value));
		return nbt;
	}
	
	public static CompoundTag effectMapToCompound(Map<ResourceLocation, Integer> inMap) {
		CompoundTag nbt = new CompoundTag();
		inMap.forEach((key, value) -> nbt.putInt(key.toString(), value));
		return nbt;
	}
}
